// controllers/auth.js
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const db = require('../config/database');

const register = async (req, res) => {
  try {
    const { username, email, password, confirmPassword } = req.body;

    // Check if email is already registered
    const emailExists = await checkEmailExists(email);
    if (emailExists) {
      return res.status(400).json({ error: 'Email sudah terdaftar.' });
    }

    // Check if password and confirmPassword match
    if (password !== confirmPassword) {
      return res.status(400).json({ error: 'Password dan konfirmasi password tidak cocok.' });
    }

    // Check password strength
    if (!isPasswordStrong(password)) {
      return res.status(400).json({
        error: 'Password harus memiliki setidaknya satu angka, satu huruf, dan panjang minimal 8 karakter.',
      });
    }

    // Hash the password
    const hashedPassword = await bcrypt.hash(password, 10);

    // Insert user into the database
    const insertQuery = 'INSERT INTO user (username, email, password, role, tanggal_daftar) VALUES (?, ?, ?, ?, ?)';
    const currentDate = new Date().toISOString().slice(0, 10);

    db.query(insertQuery, [username, email, hashedPassword, 2, currentDate], async (err, results) => {
      if (err) {
        console.error('Error inserting user:', err);
        return res.status(500).json({ error: 'Internal Server Error' });
      }

      // Retrieve user ID using email
      const userIdQuery = 'SELECT id FROM user WHERE email = ?';
      db.query(userIdQuery, [email], (err, userIdResults) => {
        if (err) {
          console.error('Error retrieving user ID:', err);
          return res.status(500).json({ error: 'Internal Server Error' });
        }

        const userId = userIdResults[0].id;

        console.log(userId);

        // Generate JWT token with user ID
        const token = jwt.sign({ userId, username, email }, process.env.JWT_SECRET, { expiresIn: '1h' });

        res.status(201).json({
          success: true,
          message: 'User successfully registered',
          token,
        });
      });
    });
  } catch (error) {
    console.error('Error registering user:', error);
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

const login = async (req, res) => {
  try {
    const { email, password } = req.body;

    // Check if the email exists in the database
    const user = await getUserByEmail(email);

    if (!user) {
      return res.status(401).json({ error: 'Login gagal. Email tidak ditemukan.' });
    }

    // Check if the password is correct
    const passwordMatch = await bcrypt.compare(password, user.password);

    if (!passwordMatch) {
      return res.status(401).json({ error: 'Login gagal. Password salah.' });
    }

    // Generate JWT token
    const token = jwt.sign(
      { userId: user.id, username: user.username, role: user.role },
      process.env.JWT_SECRET,
      { expiresIn: '1h' }
    );

    res.status(200).json({
      success: true,
      message: 'Login berhasil',
      token,
      userId: user.id,
      username: user.username,
      role: user.role,
    });
  } catch (error) {
    console.error('Error during login:', error);
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

// Helper function to get user by email from the database
const getUserByEmail = (email) => {
  return new Promise((resolve, reject) => {
    const query = 'SELECT * FROM user WHERE email = ?';
    db.query(query, [email], (err, results) => {
      if (err) {
        reject(err);
      } else {
        resolve(results[0]);
      }
    });
  });
};

// Helper function to check if email is already registered
const checkEmailExists = (email) => {
  return new Promise((resolve, reject) => {
    const query = 'SELECT * FROM user WHERE email = ?';
    db.query(query, [email], (err, results) => {
      if (err) {
        reject(err);
      } else {
        resolve(results.length > 0);
      }
    });
  });
};

// Helper function to check password strength
const isPasswordStrong = (password) => {
  // Check if password contains at least one letter, one digit, and has a minimum length of 8 characters
  const regex = /^(?=.*[A-Za-z])(?=.*\d).{8,}$/;
  return regex.test(password);
};

module.exports = { register, login };
