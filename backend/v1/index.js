const express = require('express');
const cors = require('cors');
require('dotenv').config()
const bodyParser = require('body-parser');

const app = express();
const port = process.env.PORT;

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.get('/', async (req, res) => {
    res.status(200).json({
        message: 'Welcome to Goeru'
    })
})

const authRouter = require('./routes/auth');
const guruRouter = require('./routes/guru');
const userRouter = require('./routes/user');
const paymentRouter = require('./routes/payment');

app.use('/auth/v1', authRouter);
app.use('/guru/v1', guruRouter);
app.use('/user/v1', userRouter);
app.use('/payment/v1', paymentRouter);



app.listen(port, () => {
    console.log('Your application is running on http://localhost:' + port);
});
