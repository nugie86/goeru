require('dotenv').config(); // Load environment variables from .env file

const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');

const db = require('./config/database');

const registerRoutes = require('./routes/auth');
const userRoutes = require('./routes/user');
const guruRoutes = require('./routes/guru');
const paymentRoutes = require('./routes/payment')

const app = express();
const port = process.env.PORT;

app.use(cors());
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());

// Use the register route
app.use('/auth/v1', registerRoutes);
app.use('/user/v1', userRoutes);
app.use('/guru/v1', guruRoutes);
app.use('/payment/v1', paymentRoutes);

// Handle invalid routes
app.use((req, res) => {
  res.status(404).json({ error: 'Not Found' });
});

// Handle database connection errors
db.on('error', (err) => {
  console.error('Error connecting to MySQL:', err);
});

app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
