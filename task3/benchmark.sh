#!/bin/bash
#
# this script will benchmark storage bucket and local ssd
#
# Prerequisites:
# - local ssd mounted to /mnt/local-ssd
# - storage bucket mounted to /mnt/bucket
# - ram disk mounted to /mnt/ram-disk
# - a file '1GB-file' of size in /mnt/ram-disk/


echo "#### Benchmarking storage bucket"
for i in `seq 5`; do

	echo "Doing writing..."

	# write 1 MB file to disk
	dd if=/mnt/ram-disk/1GB-file of=/mnt/bucket/1GB-file bs=1G oflag=direct

	# empty block cache
	sudo sh -c 'echo 3 > /proc/sys/vm/drop_caches'

	echo "Doing reading..."

	# read 1 MB file from disk
	dd if=/mnt/bucket/1GB-file of=/dev/null bs=1G

	# empty block cache
	sudo sh -c 'echo 3 > /proc/sys/vm/drop_caches'

	# delete the file
	rm /mnt/bucket/1GB-file

done

echo "#### Benchmarking local SSD"
for i in `seq 5`; do

	echo "Doing writing..."

	# write 1 MB file to disk
	dd if=/mnt/ram-disk/1GB-file of=/mnt/local-ssd/1GB-file bs=1G oflag=direct

	# empty block cache
	sudo sh -c 'echo 3 > /proc/sys/vm/drop_caches'

	echo "Doing reading..."

	# read 1 MB file from disk
	dd if=/mnt/local-ssd/1GB-file of=/dev/null bs=1G

	# empty block cache
	sudo sh -c 'echo 3 > /proc/sys/vm/drop_caches'

	# delete the file
	rm /mnt/local-ssd/1GB-file

done


