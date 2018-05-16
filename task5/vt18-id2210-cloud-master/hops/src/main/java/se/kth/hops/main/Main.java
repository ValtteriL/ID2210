/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hops.main;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import se.kth.hops.HopsHelper;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class Main {

  public static void main(String[] args) {
    String hopsIp = "10.132.0.2";
    String hopsPort = "8020";
    String user = "ubuntu";
    Configuration hdfsConfig = new Configuration();
    String hopsURL = "hdfs://" + hopsIp + ":" + hopsPort;
    hdfsConfig.set("fs.defaultFS", hopsURL);
    UserGroupInformation ugi = UserGroupInformation.createRemoteUser(user);
    String filePath = "/test/test.txt";
    HopsHelper.simpleCreate(ugi, hdfsConfig, filePath);
  }
}
