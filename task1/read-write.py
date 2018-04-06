#!/usr/bin/python3
# -*- coding: utf-8 -*-

from google.cloud import storage

# Authenticate using service account credentials
client = storage.Client.from_service_account_json('/home/vleh/KTH/peer-to-peer_grids/root-boi.json')

# get our bucket
bucket = client.get_bucket('korvbukett')

# create new blob
blob = bucket.blob('test2')

# fill blob with contents of a file (this will upload the file to the cloud storage)
blob.upload_from_filename('ncat.png')

# get the blob back by the name we gave it
fetched_blob = bucket.get_blob('test2')

# download the fetched blob into file (this will download the file from the cloud storage)
fetched_blob.download_to_filename('ncat2.png')
