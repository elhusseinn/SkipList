import java.util.Random;

public class SkipList {
    class Node{
      public int key;
      public Node next;
      public Node prev;
      public Node below;
      public Node above;

      public Node(int key){
            this.key = key;
            this.next = null;
            this.prev = null;
            this.below = null;
            this.above = null;
        }
    }

private Node head;
private Node tail;
private static int steps;

private final int NEG_INFIN = Integer.MIN_VALUE;
private final int POS_INFIN = Integer.MAX_VALUE;
private int height = 0;

public Random random = new Random();

public SkipList(){
    head = new Node(NEG_INFIN);
    tail = new Node(POS_INFIN);
    head.next = tail;
    tail.prev = head;
}

public Node search(int key){ // returns the node if exists and the greater node's key less than the node's key if not exist
Node n = head;
steps = 0;

while (n.below != null){ // to traverse between vertical levels
    n =n.below;

    while (key >= n.next.key){ // to traverse between horizontal nodes
        n = n.next;
        steps++;
    }
}

if(n.key != key){ // if not exists
    steps = -1;
}
return n;
}

public int returnNumOfSteps(int key){
search(key);
return steps;

}

public Node insert (int key){
    Node position = search(key);
    Node q;

    int level = -1, numOfHeads = -1;
    if (position.key == key){  // node already exist to need to override
        return position;
    }

    do{
        numOfHeads++; level++;

        canIncreaseLevel(level);

        q = position;

        while (position.above == null){ // getting the closest smaller node from the level above
            position = position.prev;
        }
        position = position.above;

        q = insertAfterAbove(position, q ,key);


    }while(random.nextBoolean()); // coin flips to head we need to go up level

return q;
}

private void canIncreaseLevel(int level){
    if (level >= height){
        height++;
        addEmptyLevel();
    }
}

private void addEmptyLevel(){ // adding and setting references to the new level
    Node newHead = new Node(NEG_INFIN);
    Node newTail = new Node(POS_INFIN);

    newHead.next = newTail;
    newHead.below = head;
    newTail.prev = newHead;
    newTail.below = tail;

    head.above = newHead;
    tail.above = newTail;

    head = newHead;
    tail = newTail;
}
private Node insertAfterAbove(Node position, Node q, int key){
    Node newNode = new Node(key);
    Node nodeBeforeNewNode = position.below.below; // getting contact with lower level, could be null

    setBeforeAndAfterReferences(q, newNode);
    setAboveAndBelowReferences(position, key, newNode, nodeBeforeNewNode);

    return newNode;

}
private void setBeforeAndAfterReferences(Node q, Node newNode){ // set references for entered node
    newNode.next = q.next;
    newNode.prev = q;
    q.next.prev = newNode;
    q.next = newNode;
}
private void setAboveAndBelowReferences(Node position, int key, Node newNode, Node nodeBeforeNewNode){ // set references for entered node
    if(nodeBeforeNewNode != null){ // lower level exists
        while (true){
            if (nodeBeforeNewNode.next.key != key){
                nodeBeforeNewNode = nodeBeforeNewNode.next;
            }
            else{
                break;
            }
        }
        newNode.below = nodeBeforeNewNode.next;
        nodeBeforeNewNode.next.above = newNode;
    }

    if(position != null){
        if(position.next.key == key){
            newNode.above = position.next;
        }
    }
}

private int getLayers(){
    return height;
}

private void printLayer(int layer){
    Node curr  = head;
    for (int i = 0; i < height - layer; i++){
        curr = curr.below;
    }
    while (curr.next.key != POS_INFIN){
        System.out.print(curr.next.key);
        System.out.print(',');
        curr = curr.next;
    }



}


private Node delete(int key){
    Node nodeTobeRemoved = search(key);

    if (nodeTobeRemoved.key != key){ // node doesn't exist
        return null;
    }

    removeReferencesToNode(nodeTobeRemoved);

    while(nodeTobeRemoved != null){
        removeReferencesToNode(nodeTobeRemoved);

        if(nodeTobeRemoved.above != null){
            nodeTobeRemoved = nodeTobeRemoved.above;
        }else{
            break;
        }
    }

return nodeTobeRemoved;
}

private void removeReferencesToNode(Node nodeToBeRemoved){
    Node afterNodeToBeRemoved = nodeToBeRemoved.next;
    Node beforeNodeToBeRemoved = nodeToBeRemoved.prev;

    beforeNodeToBeRemoved.next = afterNodeToBeRemoved;
    afterNodeToBeRemoved.prev = beforeNodeToBeRemoved;
}


public static void  main(String[] args){
    SkipList sl = new SkipList();
    sl.insert(2);
    sl.insert(10);
    sl.insert(15);
    sl.insert(16);
    sl.insert(31);
    sl.insert(71);
    sl.insert(89);
    sl.insert(91);
    sl.insert(96);

    System.out.println(sl.getLayers());
    System.out.println(sl.returnNumOfSteps(10));
    for (int i = 0; i <sl.height; i++){
        sl.printLayer(i);
        System.out.println();
    }

}

}


