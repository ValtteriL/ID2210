/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sics.nstream.storage.durable.events;

import java.util.Optional;
import se.sics.kompics.KompicsEvent;
import se.sics.kompics.util.Identifier;
import se.sics.ktoolbox.util.identifiable.basic.PairIdentifier;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class DStorageRead {

  public static class Request implements KompicsEvent, DStreamEvent {

    public final PairIdentifier streamId;
    public final long readPos;
    public final int readLength;

    public Request(PairIdentifier streamId, long readPos, int readLength) {
      this.streamId = streamId;
      this.readPos = readPos;
      this.readLength = readLength;
    }

    @Override
    public PairIdentifier getStreamId() {
      return streamId;
    }

    @Override
    public Identifier getEndpointId() {
      return streamId.id1;
    }

    @Override
    public Identifier getFileId() {
      return streamId.id2;
    }
    
    public Response respond(Optional<byte[]> result) {
      return new Response(this, result);
    }
  }

  public static class Response implements KompicsEvent, DStreamEvent {

    public final Request req;
    public final Optional<byte[]> result;

    public Response(Request req, Optional<byte[]> result) {
      this.req = req;
      this.result = result;
    }

    @Override
    public PairIdentifier getStreamId() {
      return req.getStreamId();
    }

    @Override
    public Identifier getEndpointId() {
      return req.getEndpointId();
    }

    @Override
    public Identifier getFileId() {
      return req.getFileId();
    }
  }
}
