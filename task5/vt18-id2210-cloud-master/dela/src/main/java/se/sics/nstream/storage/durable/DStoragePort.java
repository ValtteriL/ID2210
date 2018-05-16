/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sics.nstream.storage.durable;

import se.sics.kompics.PortType;
import se.sics.nstream.storage.durable.events.DStorageWrite;
import se.sics.nstream.storage.durable.events.DStorageRead;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class DStoragePort extends PortType {

  {
    request(DStorageRead.Request.class);
    indication(DStorageRead.Response.class);
    request(DStorageWrite.Request.class);
    indication(DStorageWrite.Response.class);
  }
}
