const axios = require('axios');

// Set your server key (Note: Server key for sandbox and production mode are different)
const serverKey = 'SB-Mid-server-uAjuMuQBi0bd8R6U8tnBJQ7D';
// Set true for production, set false for sandbox
const isProduction = false;

const apiUrl = isProduction
  ? 'https://app.midtrans.com/snap/v1/transactions'
  : 'https://app.sandbox.midtrans.com/snap/v1/transactions';

// Charge API function using axios
async function chargeAPI(requestBody) {
  try {
    const response = await axios.post(apiUrl, requestBody, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': 'Basic ' + Buffer.from(serverKey + ':').toString('base64'),
      },
    });

    return {
      body: response.data,
      httpCode: response.status,
    };
  } catch (error) {
    return {
      body: error.response.data,
      httpCode: error.response.status,
    };
  }
}

module.exports = {
  chargeAPI,
};
