const express = require('express');
const router = express.Router();
const controller = require('../controllers/payment');

// Endpoint for handling charge requests
router.post('/charge', async (req, res) => {
  try {
    const requestBody = req.body;

    // Call charge API using the controller
    const chargeResult = await controller.chargeAPI(requestBody);

    // Set the response status and send the result
    res.status(chargeResult.httpCode).json(chargeResult.body);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Internal Server Error' });
  }
});

module.exports = router;
