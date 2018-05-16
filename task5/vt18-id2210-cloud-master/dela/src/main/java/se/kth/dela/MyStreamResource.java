/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.dela;

import se.sics.nstream.storage.durable.util.DStreamResource;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class MyStreamResource implements DStreamResource {

  @Override
  public String getSinkName() {
    return "myStream";
  }
}
