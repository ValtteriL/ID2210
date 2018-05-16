/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.dela;

import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Negative;
import se.sics.nstream.storage.durable.DStoragePort;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class MyStorageComp extends ComponentDefinition {
  Negative<DStoragePort> resourcePort = provides(DStoragePort.class);
  
  public MyStorageComp(Init init) {
  }
  
  public static class Init extends se.sics.kompics.Init<MyStorageComp> {
    public final MyStreamEndpoint endpoint;
    public final MyStreamResource resource;
    
    public Init(MyStreamEndpoint endpoint, MyStreamResource resource) {
      this.endpoint = endpoint;
      this.resource = resource;
    }
  }
  
  
}
