from flask import Flask, jsonify

app = Flask(__name__)

# Your recommendation code here
import numpy as np
from recsysNN_utils import *
from tensorflow.keras.models import load_model

# Load Data, set configuration variables
item_train, user_train, y_train, item_features, user_features, item_vecs, movie_dict = load_data()

# Model sudah di-load sebelumnya (gunakan path yang sesuai)
model = load_model('my_model (1).h5')

from sklearn.preprocessing import StandardScaler, MinMaxScaler
scalerUser = StandardScaler()
scalerUser.fit(user_train)
scalerItem = StandardScaler()
scalerItem.fit(item_train)
scalerTarget = MinMaxScaler((-1, 1))
scalerTarget.fit(y_train.reshape(-1, 1))
y_train = scalerTarget.transform(y_train.reshape(-1, 1))

# Sample user input
new_user_id = 5000
new_age = 20
new_Mathematics = 5.0
new_Physics = 5.0
new_Biology = 0.0
new_Chemistry = 0.0
new_Economy = 0.0
new_Sociology = 0.0
new_Geography = 0.0
new_History = 0.0
new_Anthropology = 0.0

user_vec = np.array([[new_user_id, new_age, new_Mathematics,
                      new_Physics, new_Biology, new_Chemistry,
                      new_Economy, new_Sociology, new_Geography,
                      new_History, new_Anthropology
                      ]])

# Generate and replicate the user vector to match the number of movies in the data set.
user_vecs = gen_user_vecs(user_vec, len(item_vecs))

# Scale our user and item vectors
suser_vecs = scalerUser.transform(user_vecs)
sitem_vecs = scalerItem.transform(item_vecs)

# Make a prediction
y_p = model.predict([suser_vecs[:, 2:], sitem_vecs[:, 1:]])

# Unscale y prediction
y_pu = scalerTarget.inverse_transform(y_p)

# Sort the results, highest prediction first
sorted_index = np.argsort(-y_pu, axis=0).reshape(-1).tolist()  # Negate to get the largest rating first
sorted_ypu = y_pu[sorted_index]
sorted_items = item_vecs[sorted_index]  # Using unscaled vectors for display

# Create a list of dictionaries for each teacher
result_list = []
for yp, item in zip(sorted_ypu[:10], sorted_items[:10]):
    result_dict = {
        "y_p": float(yp[0]),
        "teacher_id": int(item[0]),
        "rating_ave": float(item[1]),
        "name": movie_dict[int(item[0])]["title"],
        "fields": movie_dict[int(item[0])]["genres"]
    }
    result_list.append(result_dict)

import json

# Convert the list of dictionaries to JSON
json_output = json.dumps(result_list, indent=2)


@app.route('/rekomendasiGuru', methods=['GET'])
def get_rekomendasi_guru():
    return jsonify(result_list)

if __name__ == '__main__':
    app.run(debug=True)