const express = require('express');
const paymentController = require('../controllers/payment');
const authMiddleware = require('../middleware/user');

const router = express.Router();

router.post('/charge', paymentController.charge);

module.exports = router;
