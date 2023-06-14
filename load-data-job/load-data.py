import redis
import os
import json

REDIS_HOST = os.getenv('REDIS_HOST')
REDIS_PASSWORD = os.environ.get('REDIS_PASSWORD')

print("~" + REDIS_HOST)
if (REDIS_PASSWORD != None):
    print("~" + REDIS_PASSWORD)

words_db = redis.Redis(host=REDIS_HOST, password=REDIS_PASSWORD, port=6379, db=0)
labels_db = redis.Redis(host=REDIS_HOST, password=REDIS_PASSWORD, port=6379, db=1)


with open('reuters_word_index.json') as f:
  data = json.load(f)
  for (key, value) in data.items():
    print("Adding key: {}, value: {} to words_db".format(key, value))
    words_db.set(key, value)

labels = ['cocoa','grain','veg-oil','earn','acq','wheat','copper','housing','money-supply',
        'coffee','sugar','trade','reserves','ship','cotton','carcass','crude','nat-gas',
        'cpi','money-fx','interest','gnp','meal-feed','alum','oilseed','gold','tin',
        'strategic-metal','livestock','retail','ipi','iron-steel','rubber','heat','jobs',
        'lei','bop','zinc','orange','pet-chem','dlr','gas','silver','wpi','hog','lead']

for (key, value) in enumerate(labels):
    print("Adding key: {}, value: {} to labels_db".format(key, value))
    labels_db.set(key, value)
