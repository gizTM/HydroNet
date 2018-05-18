'use strict';
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

// example http trigger
// exports.addMessage = functions.https.onRequest((req, res) => {
//   const original = req.query.text;
//   return admin.database().ref('/messages').push({original: original}).then((snapshot) => {
//     return res.redirect(303, snapshot.ref.toString());
//   });
// });

// example edit realtime database
// exports.makeUppercase = functions.database.ref('/messages/{pushId}/original')
//     .onCreate((snapshot, context) => {
//       const original = snapshot.val();
//       console.log('Uppercasing', context.params.pushId, original);
//       const uppercase = original.toUpperCase();
//       return snapshot.ref.parent.child('uppercase').set(uppercase);
//     });

exports.saveSensorData = functions.https.onRequest((req, res) => {
  const water = req.query.water;
  const ph = req.query.pH;
  const ec = req.query.ec;
  const uid = req.query.uid;
  var sensorDataKey = admin.database().ref('/sensorData/'+uid).push().key;
  var now = String(Date.now());
  admin.database().ref('/sensorData/'+uid+'/'+sensorDataKey).set({
    timestamp: now,
    waterLevel: parseFloat(water),
    pHLevel: parseFloat(ph),
    ecLevel: parseFloat(ec)
  });
  admin.database().ref('/userPlants/'+uid).on('value',function(snapshot) {
    snapshot.forEach(function(childSnapshot) {
      var upid = childSnapshot.key;
      admin.database().ref('/growHistories/'+upid).orderByChild('harvested').equalTo(false).once('value',function(snapshot) {
        snapshot.forEach(function(childSnapshot) {
          var ghid = childSnapshot.key;
          var updates = {};
          updates['/sensorHistory/'+sensorDataKey+'/'+ghid] = true;
          updates['/growHistories/'+upid+'/'+ghid+'/sensorData/'+sensorDataKey] = true;
          admin.database().ref().update(updates);
        });
      });
    });
  });
  return admin.database().ref('/notifications/'+uid).push({
    title: 'HydroNet',
    message: 'Your farm status is water: '+water+', pH: '+ph+', EC: '+ec,
    type: 0,
    timeStamp: now,
    read: false,
    senderId: 'CaUPAtrqmCeAhSqbrhBKfYu5b5D3',
    water: parseFloat(water),
    pH: parseFloat(ph),
    ec: parseFloat(ec)
  }).then((snapshot) => {
    return res.redirect(303, snapshot.ref.toString());
  });
});

exports.alertWater = functions.https.onRequest((req, res) => {
  const water = req.query.waterlevel;
  return admin.database().ref('/notifications/'+uid).push({
    title: 'HydroNet',
    message: 'Your farm water level is '+water,
    type: 0,
    timeStamp: String(Date.now()),
    read: false,
    senderId: 'CaUPAtrqmCeAhSqbrhBKfYu5b5D3',
    waterlevel: water
  }).then((snapshot) => {
    return res.redirect(303, snapshot.ref.toString());
  });
});

exports.alertpH = functions.https.onRequest((req, res) => {
  const ph = req.query.pH;
  return admin.database().ref('/notifications/'+uid).push({
    title: 'HydroNet',
    message: 'Your farm pH level is '+ph,
    type: 0,
    timeStamp: String(Date.now()),
    read: false,
    senderId: 'CaUPAtrqmCeAhSqbrhBKfYu5b5D3',
    pH: ph
  }).then((snapshot) => {
    return res.redirect(303, snapshot.ref.toString());
  });
});

exports.alertEC = functions.https.onRequest((req, res) => {
  const ec = req.query.ec;
  return admin.database().ref('/notifications/'+uid).push({
    title: 'HydroNet',
    message: 'Your farm EC level is '+ec,
    type: 0,
    timeStamp: String(Date.now()),
    read: false,
    senderId: 'CaUPAtrqmCeAhSqbrhBKfYu5b5D3',
    ec: ec
  }).then((snapshot) => {
    return res.redirect(303, snapshot.ref.toString());
  });
});

exports.addNotification = functions.database.ref('/notifications/{receiverUid}/{notificationId}')
    .onCreate((snapshot, context) => {
      const receiverUid = context.params.receiverUid;
      const notificationObject = snapshot.val();
      const title = notificationObject.title;
      const senderId = notificationObject.senderId;
      const message = notificationObject.message;
      console.log('receiverUid: ',receiverUid,'\ntitle: ',title,'\nsenderId: ',senderId,'\nmessage: ',message);
      if(senderId === receiverUid) return console.log('Receiver is sender!');
      const getDeviceTokensPromise = admin.database()
          .ref(`/users/${receiverUid}/notificationTokens`).once('value');
      const getSenderProfilePromise = admin.auth().getUser(senderId);
      let tokensSnapshot;
      let tokens;
      return Promise.all([getDeviceTokensPromise, getSenderProfilePromise]).then(results => {
        tokensSnapshot = results[0];
        const sender = results[1];
        if (!tokensSnapshot.hasChildren()) {
          return console.log('There are no notification tokens to send to.');
        }
        console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
        console.log('Fetched sender profile', sender);

        const payload = {
          notification: {
            title: title,
            body: message,
            // icon: sender.photoURL
          }
        };
        tokens = Object.keys(tokensSnapshot.val());
        return admin.messaging().sendToDevice(tokens, payload);
      }).then((response) => {
        const tokensToRemove = [];
        response.results.forEach((result, index) => {
          const error = result.error;
          if (error) {
            console.error('Failure sending notification to', tokens[index], error);
            if (error.code === 'messaging/invalid-registration-token' ||
                error.code === 'messaging/registration-token-not-registered') {
              tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
            }
          }
        });
        return Promise.all(tokensToRemove);
      });
    });

exports.notifyWeekly = functions.https.onRequest((req, res) => {
  const uid = req.query.uid;
  return admin.database().ref('/weeklyNotifications/'+uid).push({
    notice: 'you are notified'
  }).then((snapshot) => {
    return res.redirect(303, snapshot.ref.toString());
  });
});