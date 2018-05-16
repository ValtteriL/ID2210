/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.dela;

import se.sics.nstream.storage.durable.DurableStorageProvider;
import se.sics.nstream.storage.durable.util.DStreamEndpoint;
import se.sics.nstream.storage.durable.util.DStreamResource;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class MyStorageProvider implements DurableStorageProvider {

  public final String name;
  public final MyStreamEndpoint endpoint;

  public MyStorageProvider(MyStreamEndpoint endpoint) {
    this.name = "myStorageProvider";
    this.endpoint = endpoint;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public DStreamEndpoint getEndpoint() {
    return endpoint;
  }

  @Override
  public Class getStorageDefinition() {
    return MyStorageComp.class;
  }

  @Override
  public MyStorageComp.Init initiate(DStreamResource resource) {
    return new MyStorageComp.Init(endpoint, (MyStreamResource) resource);
  }
}
