const axios = require('axios');
const dotenv = require('dotenv');
dotenv.config();

const serverKey = process.env.SERVER_KEY;
const isProduction = false;
const apiUrl = isProduction ?
  'https://app.midtrans.com/snap/v1/transactions' :
  'https://app.sandbox.midtrans.com/snap/v1/transactions';

const charge = async (req, res) => {
  try {
    // Call charge API using axios
    const chargeResult = await chargeAPI(apiUrl, serverKey, req.body);

    // Set the response status code
    res.status(chargeResult.httpCode);
    // Send the response body
    res.send(chargeResult.body);
  } catch (error) {
    console.error('Error:', error);
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

/**
 * Call charge API using Axios
 * @param {string} api_url
 * @param {string} server_key
 * @param {object} request_body
 */
const chargeAPI = async (api_url, server_key, request_body) => {
    try {
      const response = await axios.post(api_url, request_body, {
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Authorization': 'Basic ' + Buffer.from(server_key + ':').toString('base64'),
        },
      });
  
      return {
        body: response.data,
        httpCode: response.status,
      };
    } catch (error) {
      if (error.response && error.response.data) {
        // Handle specific error cases
        if (error.response.status === 400 && error.response.data.error_messages) {
          console.error('JSON Parsing Error:', error.response.data.error_messages);
          return {
            body: { error: 'Invalid JSON format' },
            httpCode: 400,
          };
        }
      }
  
      // For other errors, return the standard error response
      return {
        body: error.response.data,
        httpCode: error.response.status,
      };
    }
  };

module.exports = {
  charge,
  chargeAPI,
};
