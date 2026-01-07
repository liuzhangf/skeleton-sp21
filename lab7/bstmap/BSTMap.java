package bstmap;

import edu.princeton.cs.algs4.BST;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V > implements Map61B<K, V>{

    private int size;
    private Node root;

    private class Node{

        K key;
        V data;
        Node  left;
        Node  right;

        Node(K key, V data) {
            this.key = key;
            this.data = data;
            this.left = null;
            this.right = null;
        }

    }

    public BSTMap() {
        this.size = 0;
        this.root = null;
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public boolean containsKey (K key) {
        return search (root, key);
    }

    private boolean search (Node root, K key) {
        if (root == null) {
            return false;
        } else if (key.compareTo (root.key) == 0) {
            return true;
        }
        else if (key.compareTo (root.key) < 0) {
            return search (root.left, key);
        }
        else {
            return search (root.right, key);
        }
    }

    @Override
    public V get(K key){
        return search2(root, key);
    }

    private V search2 (Node root, K key) {
        if (root == null) {
            return null;
        } else if (key.compareTo (root.key) == 0) {
            return root.data;
        }
        else if (key.compareTo (root.key) < 0) {
            return search2 (root.left, key);
        }
        else {
            return search2 (root.right, key);
        }
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public void put(K key, V value){
        if (root == null) {
            root = new Node (key, value);
            size++;
        }
        search3(root, key, value);
    }

    private void search3 (Node root, K key, V value) {

        if (root == null) {
            return;
        }

        else if (key.compareTo (root.key) > 0) {
            if(root.right == null) {
                Node temp = new Node(key, value);
                root.right = temp;
                size++;
            }
            else search3 (root.right, key, value);
        }

        else if (key.compareTo (root.key) < 0) {
            if(root.left == null) {
                Node temp = new Node (key, value);
                root.left = temp;
                size++;
            }
            else search3 (root.left, key, value);
        }
        else  if(key.compareTo (root.key) == 0) {
            root.data = value;
        }
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public V remove(K key) {

        if (key == null) {
            return null;
        }

        V val = search2(root, key);

        if (root.key == key) {
            root = null;
        }
        else {
            if (val != null) {
                delete(root, key);
            }
        }

        size --;
        return val;
    }

    private void delete(Node root, K key) {
        if (key.compareTo (root.left.key) == 0) {
            int cnt = countSubTree(root.left);
            if (cnt == 0) {
                root.left = null;
            }
            else if (cnt == 1) {
                root.left = root.left.left;
            }
            else if (cnt == 4) {
                root.left = root.left.right;
            }
            else {
                Node temp = getLeftest(root.left);
                root.left = temp;
            }
        }

        else if (key.compareTo (root.right.key) == 0) {
            int cnt = countSubTree(root.right);
            if (cnt == 0) {
                root.right = null;
            }
            else if (cnt == 1) {
                root.right = root.right.left;
            }
            else if (cnt == 4) {
                root.right = root.right.right;
            }
            else {
                Node temp = getLeftest(root.right);
                root.right = temp;
            }
        }
    }

    private int countSubTree(Node root) {
        if (root.left == null && root.right == null) {
            return 0;
        }
        else if (root.left != null && root.right != null) {
            return 2;
        }
        else if (root.left != null && root.right == null) {
            return 1;
        }
        else {
            return 4;
        }
    }


    public Node getLeftest(Node root) {
        if (root.right != null) {
            root = root.right;
        }
        return root;
    }

    @Override
    public V remove(K key, V value) {
        if (key == null) {
            return null;
        }

        size --;
        V val = search2(root, key);

        if (root.key == key) {
            root = null;
        }
        else {
            if (val != null) {
                delete(root, key);
            }
        }

        return val;
    }

    @Override
    public Iterator<K> iterator(){
        return new BSTIterator<>();
    }

    private class BSTIterator<K> implements Iterator<K> {

        int cnt = 0;
        Node node = root;

        @Override
        public boolean hasNext(){
            if (size <= cnt ) {
                return false;
            }
            else {
                return true;
            }
        }

        @Override
        public K next(){
            while (hasNext()){
                K num[] = (K[]) getInorderAllElements();
                return num[cnt ++];
            }
            return null;
        }

    }

    private K[] getInorderAllElements() {
        List<K> inorderList = new ArrayList<>();
        collectInorder(root, inorderList);
        return (K[]) inorderList.toArray(new Object[0]);
    }

    private void collectInorder(Node node, List<K> resultList) {
        if (node == null) {
            return;
        }
        collectInorder(node.left, resultList);
        resultList.add(node.key);
        collectInorder(node.right, resultList);
    }
}