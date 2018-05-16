/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sics.nstream.storage.durable.events;

import se.sics.kompics.KompicsEvent;
import se.sics.kompics.util.Identifier;
import se.sics.ktoolbox.util.identifiable.basic.PairIdentifier;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class DStorageWrite {

  public static class Request implements KompicsEvent, DStreamEvent {

    public final PairIdentifier streamId;
    public final long pos;
    public final byte[] value;

    public Request(PairIdentifier streamId, long pos, byte[] value) {
      this.streamId = streamId;
      this.pos = pos;
      this.value = value;
    }

    public Response respond(int bytesWritten) {
      return new Response(this, bytesWritten);
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
  }

  public static class Response implements KompicsEvent, DStreamEvent {

    public final Request req;
    public final int bytesWritten;

    public Response(Request req, int bytesWritten) {
      this.req = req;
      this.bytesWritten = bytesWritten;
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
