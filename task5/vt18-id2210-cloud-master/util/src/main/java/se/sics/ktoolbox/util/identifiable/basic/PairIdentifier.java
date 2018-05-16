package se.sics.ktoolbox.util.identifiable.basic;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Objects;
import se.sics.kompics.util.Identifier;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class PairIdentifier<A extends Identifier,B extends Identifier> implements Identifier {
  public final A id1;
  public final B id2;
  
  public PairIdentifier(A id1, B id2) {
    this.id1 = id1;
    this.id2 = id2;
  }

  @Override
  public int partition(int nrPartitions) {
    if(nrPartitions > Integer.MAX_VALUE / 2) {
      throw new RuntimeException("fix this");
    }
    int partition = (id1.partition(nrPartitions) + id2.partition(nrPartitions)) % nrPartitions;
    return partition;
  }

  @Override
  public int compareTo(Identifier o) {
    int result;
    PairIdentifier that = (PairIdentifier) o;
    result = this.id1.compareTo(that.id1);
    if(result == 0) {
      result = this.id2.compareTo(that.id2);
    }
    return result;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + Objects.hashCode(this.id1);
    hash = 19 * hash + Objects.hashCode(this.id2);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PairIdentifier<?, ?> other = (PairIdentifier<?, ?>) obj;
    if (!Objects.equals(this.id1, other.id1)) {
      return false;
    }
    if (!Objects.equals(this.id2, other.id2)) {
      return false;
    }
    return true;
  }
}
