#!/usr/bin/python3
# -*- coding: utf-8 -*-

# this script will benchmark reading and writing the cloud storage
#
# benchmarking is done as follows:
# 1 mb file is sequentially uploaded to cloud 5 times
# 1 mb file is sequentially downloaded from cloud 5 times
# 256 kb file is parallelly uploaded to cloud by 4 threads 5 times
# 256 kb file is parallelly downloaded from cloud by 4 threads 5 times
#
# the elapsed durations are measured for each 'time' 
# the measurements thus correspond to the duration of moving a total of 1mb of data


import threading
import datetime
import os
from google.cloud import storage


# function to upload filename to bucket as benchmark-i
def do_upload(bucket, i, filename):
    blob = bucket.blob('benchmark-{}'.format(i))
    blob.upload_from_filename(filename)

# function to download benchmark-i from bucket to disk
def do_download(bucket, i):
    blob = bucket.get_blob('benchmark-{}'.format(i))
    blob.download_to_filename('benchmark-{}'.format(i))


# lists to store elapsed times
sequential_upload = []
sequential_download = []
parallel_upload = []
parallel_download = []

# authenticate using service account credentials
client = storage.Client.from_service_account_json('/home/vleh/KTH/peer-to-peer_grids/root-boi.json')
bucket = client.get_bucket('korvbukett') # get our bucket

# do 5 sequential uploads of 1Mb file
print("Doing sequential uploads")
for i in range(5):
    print("{}...".format(i))
    a = datetime.datetime.now()
    do_upload(bucket, i, "1mb-file") # do the thing
    b = datetime.datetime.now()
    sequential_upload.append(b-a) # append elapsed time to list


# do 5 sequential downloads of 1Mb file
print("Doing sequential downloads")
for i in range(5):
    print("{}...".format(i))
    a = datetime.datetime.now()
    do_download(bucket, i) # do the thing
    b = datetime.datetime.now()
    sequential_download.append(b-a) # append elapsed time to list

# remove blobs from cloud and disk
for i in range(5):
    bucket.get_blob('benchmark-{}'.format(i)).delete()
    os.remove('benchmark-{}'.format(i))

# do parallel upload (4 threads parallelly upload 256kb, 5 times)
print("Doing parallel uploads")
for _ in range(5):
    print("{}...".format(_))

    threads = []
    a = datetime.datetime.now()
    
    # create 4 threads
    for i in range(4):
        t = threading.Thread(target=do_upload, args=[bucket, i, "256kb-file"])
        threads.append(t)
        t.start()
    
    # wait for the threads to finish
    for t in threads:
        t.join()
    
    b = datetime.datetime.now()
    parallel_upload.append(b-a) # append elapsed time to list

    # delete the uploaded blobs from cloud storage
    for i in range(4):
        bucket.get_blob('benchmark-{}'.format(i)).delete()


# upload 256kb files to be downloaded in download benchmark up next
for i in range(4):
    do_upload(bucket, i, "256kb-file")

# do parallel download (4 threads parallelly download 256kb, 5 times)
print("Doing parallel downloads")
for _ in range(5):
    print("{}...".format(_))

    threads = []
    a = datetime.datetime.now()

    # create 4 threads
    for i in range(4):
        t = threading.Thread(target=do_download, args=[bucket, i])
        threads.append(t)
        t.start()

    # wait for the threads to finish
    for t in threads:
        t.join()

    b = datetime.datetime.now()
    parallel_download.append(b-a) # append elapsed time to list

    # remove downloaded files from disk
    for i in range(4):
        os.remove('benchmark-{}'.format(i))


# delete the uploaded blobs from cloud storage
for i in range(4):
    bucket.get_blob('benchmark-{}'.format(i)).delete()



# print results
print("-- Results in seconds --")
print("Sequential upload: " + ", ".join([str(x.total_seconds()) for x in sequential_upload]) + " mean = {}".format(sum(sequential_upload, datetime.timedelta()).total_seconds()/float(len(sequential_upload))))
print("Sequential download: " + ", ".join([str(x.total_seconds()) for x in sequential_download]) + " mean = {}".format(sum(sequential_download, datetime.timedelta()).total_seconds()/float(len(sequential_download))))
print("Parallel upload: " + ", ".join([str(x.total_seconds()) for x in parallel_upload]) + " mean = {}".format(sum(parallel_upload, datetime.timedelta()).total_seconds()/float(len(parallel_upload))))
print("Parallel download: " + ", ".join([str(x.total_seconds()) for x in parallel_download]) + " mean = {}".format(sum(parallel_download, datetime.timedelta()).total_seconds()/float(len(parallel_download))))