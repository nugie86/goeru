const express = require('express');
const router = express.Router();
const userController = require('../controllers/user');
const verifyTokenUser = require('../middleware/user');

// Rute yang tidak memerlukan token
router.get('/allGuru', userController.getAllGuru);
router.get('/:id', userController.getGuruById);
router.post('/rating', verifyTokenUser, userController.postRating);
router.post('/preferensi', verifyTokenUser, userController.postPreferensi);

module.exports = router;
