/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sics.nstream.storage.durable.events;

import se.sics.kompics.util.Identifier;
import se.sics.ktoolbox.util.identifiable.basic.PairIdentifier;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public interface DStreamEvent {
  public PairIdentifier getStreamId();
  public Identifier getFileId();
  public Identifier getEndpointId();
}
