/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hops;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PrivilegedExceptionAction;
import java.util.Optional;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class HopsHelper {
private final static Logger LOG = LoggerFactory.getLogger(HopsHelper.class);

  public static boolean canConnect(final Configuration hdfsConfig) {
    LOG.debug("testing hdfs connection");
    try (FileSystem fs = FileSystem.get(hdfsConfig)) {
      LOG.debug("getting status");
      FsStatus status = fs.getStatus();
      LOG.debug("got status");
      return true;
    } catch (IOException ex) {
      LOG.info("could not connect:{}", ex);
      return false;
    }
  }

  public static long length(UserGroupInformation ugi, final Configuration hdfsConfig, final String filePath) {
    LOG.debug("getting length of file:{}", filePath);

    try {
      Long result = ugi.doAs(new PrivilegedExceptionAction<Long>() {
        @Override
        public Long run() {
          long length = -1;
          try (DistributedFileSystem fs = (DistributedFileSystem) FileSystem.get(hdfsConfig)) {
            FileStatus status = fs.getFileStatus(new Path(filePath));
            if (status.isFile()) {
              length = status.getLen();
            }
            return length;
          } catch (FileNotFoundException ex) {
            return -1l;
          } catch (IOException ex) {
            LOG.warn("could not get size of file:{}", ex);
            return -2l;
          }
        }
      });
      LOG.trace("op completed");
      return result;
    } catch (IOException | InterruptedException ex) {
      LOG.error("unexpected exception:{}", ex);
      return -2l;
    }
  }
  
  public static Boolean delete(UserGroupInformation ugi, final Configuration hdfsConfig, final String filePath) {
    LOG.info("deleting file:{}", new Object[]{filePath});
    try {
      Boolean result = ugi.doAs(new PrivilegedExceptionAction<Boolean>() {
        @Override
        public Boolean run() {
          try (FileSystem fs = FileSystem.get(hdfsConfig)) {
            fs.delete(new Path(filePath), false);
            return true;
          } catch (IOException ex) {
            LOG.warn("could not delete file:{}", ex.getMessage());
            return false;
          }
        }
      });
      LOG.trace("op completed");
      return result;
    } catch (IOException | InterruptedException ex) {
      LOG.error("unexpected exception:{}", ex);
      return false;
    }
  }

  public static Boolean simpleCreate(UserGroupInformation ugi, final Configuration hdfsConfig, final String filePath) {
    LOG.info("creating file:{}", filePath);
    try {
      Boolean result = ugi.doAs(new PrivilegedExceptionAction<Boolean>() {
        @Override
        public Boolean run() {
          try (FileSystem fs = FileSystem.get(hdfsConfig)) {
            Path path = new Path(filePath);
            if (!fs.isDirectory(path.getParent())) {
              fs.mkdirs(path.getParent());
            }
            if (fs.isFile(new Path(filePath))) {
              return false;
            }
            try (FSDataOutputStream out = fs.create(path, (short) 1)) {
              return true;
            }
          } catch (IOException ex) {
            LOG.warn("could not write file:{}", ex);
            return false;
          }
        }
      });
      LOG.trace("op completed");
      return result;
    } catch (IOException | InterruptedException ex) {
      LOG.error("unexpected exception:{}", ex);
      return false;
    }
  }

  public static Optional<byte[]> read(UserGroupInformation ugi, final Configuration hdfsConfig, final String filePath, 
    final long position, final int readLength) {
    LOG.debug("reading from file:{}", filePath);
    try {
      Optional<byte[]> result = ugi.doAs(new PrivilegedExceptionAction<Optional<byte[]>>() {
        @Override
        public Optional<byte[]> run() {
          try (DistributedFileSystem fs = (DistributedFileSystem) FileSystem.get(hdfsConfig);
            FSDataInputStream in = fs.open(new Path(filePath))) {
            byte[] byte_read = new byte[readLength];
            in.readFully(position, byte_read);
            return Optional.of(byte_read);
          } catch (IOException ex) {
            LOG.warn("could not read file:{} ex:{}", filePath, ex);
            return Optional.empty();
          }
        }
      });
      LOG.trace("op completed");
      return result;
    } catch (IOException | InterruptedException ex) {
      LOG.error("unexpected exception:{}", ex);
      return Optional.empty();
    }
  }

  public static boolean append(UserGroupInformation ugi, final Configuration hdfsConfig, final String filePath,
    final byte[] data) {
    LOG.debug("appending to file:{}", filePath);
    try {
      Boolean result = ugi.doAs(new PrivilegedExceptionAction<Boolean>() {
        @Override
        public Boolean run() {
          try (DistributedFileSystem fs = (DistributedFileSystem) FileSystem.get(hdfsConfig);
            FSDataOutputStream out = fs.append(new Path(filePath))) {
            out.write(data);
            return true;
          } catch (IOException ex) {
            LOG.warn("could not append to file:{} ex:{}", filePath, ex);
            return false;
          }
        }
      });
      LOG.trace("op completed");
      return result;
    } catch (IOException | InterruptedException ex) {
      LOG.error("unexpected exception:{}", ex);
      return false;
    }
  }
  
  public static Boolean flush(UserGroupInformation ugi, final Configuration hdfsConfig, final String filePath) {
    LOG.debug("flushing file:{}", filePath);
    try {
      Boolean result = ugi.doAs(new PrivilegedExceptionAction<Boolean>() {
        @Override
        public Boolean run() {
          try (DistributedFileSystem fs = (DistributedFileSystem) FileSystem.get(hdfsConfig);
            FSDataOutputStream out = fs.append(new Path(filePath))) {
            out.hflush();
            return true;
          } catch (IOException ex) {
            LOG.warn("could not append to file:{} ex:{}", filePath, ex.getMessage());
            return false;
          }
        };
      });
      LOG.trace("op completed");
      return result;
    } catch (IOException | InterruptedException ex) {
      LOG.error("unexpected exception:{}", ex);
      return false;
    }
  }
  
  public static Long blockSize(UserGroupInformation ugi, final Configuration hdfsConfig, final String filePath) {
    LOG.debug("block size for file:{}", filePath);
    try {
      Long result = ugi.doAs(new PrivilegedExceptionAction<Long>() {
        @Override
        public Long run() {
          try (DistributedFileSystem fs = (DistributedFileSystem) FileSystem.get(hdfsConfig)) {
            FileStatus status = fs.getFileStatus(new Path(filePath));
            long hdfsBlockSize = fs.getDefaultBlockSize();
            if (status.isFile()) {
              hdfsBlockSize = status.getBlockSize();
            }
            return hdfsBlockSize;
          } catch (IOException ex) {
            LOG.warn("could not append to file:{} ex:{}", filePath, ex.getMessage());
            return -1l;
          }
        };
      });
      LOG.trace("op completed");
      return result;
    } catch (IOException | InterruptedException ex) {
      LOG.error("unexpected exception:{}", ex);
      return -1l;
    }
  }
}
