const express = require('express');
const router = express.Router();

const userControllers = require('../controllers/user');
const authMiddleware = require('../middleware/user');

// Endpoint: GET /user/v1/allGuru
router.get('/allGuru', userControllers.getAllGuru);
router.get('/:idGuru', userControllers.getGuruById);
router.post('/preferensi',authMiddleware, userControllers.saveOrUpdatePreferensi);
router.post('/rating',authMiddleware, userControllers.addRating);


module.exports = router;