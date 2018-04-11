#!/bin/bash
#
# this script will benchmark the storage
# storage must be mounted to directory 'taskthree'


for i in `seq 5`; do

	echo "Doing writing..."

	# write 1 MB file to disk
	dd if=/dev/zero of=taskthree/tempfile bs=1024 count=1024 conv=fdatasync,notrunc status=progress

	# empty block cache
	sudo sh -c 'echo 3 > /proc/sys/vm/drop_caches'

	echo "Doing reading..."

	# read 1 MB file from disk
	dd if=taskthree/tempfile of=/dev/null bs=1024 count=1024 status=progress

	# empty block cache
	sudo sh -c 'echo 3 > /proc/sys/vm/drop_caches'

	# delete the file
	rm taskthree/tempfile

done

