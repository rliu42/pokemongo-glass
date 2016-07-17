package com.fasterxml.jackson.databind.util;

public abstract class PrimitiveArrayBuilder<T> {
    static final int INITIAL_CHUNK_SIZE = 12;
    static final int MAX_CHUNK_SIZE = 262144;
    static final int SMALL_CHUNK_SIZE = 16384;
    protected Node<T> _bufferHead;
    protected Node<T> _bufferTail;
    protected int _bufferedEntryCount;
    protected T _freeBuffer;

    static final class Node<T> {
        final T _data;
        final int _dataLength;
        Node<T> _next;

        public Node(T data, int dataLen) {
            this._data = data;
            this._dataLength = dataLen;
        }

        public T getData() {
            return this._data;
        }

        public int copyData(T dst, int ptr) {
            System.arraycopy(this._data, 0, dst, ptr, this._dataLength);
            return ptr + this._dataLength;
        }

        public Node<T> next() {
            return this._next;
        }

        public void linkNext(Node<T> next) {
            if (this._next != null) {
                throw new IllegalStateException();
            }
            this._next = next;
        }
    }

    protected abstract T _constructArray(int i);

    protected PrimitiveArrayBuilder() {
    }

    public int bufferedSize() {
        return this._bufferedEntryCount;
    }

    public T resetAndStart() {
        _reset();
        return this._freeBuffer == null ? _constructArray(INITIAL_CHUNK_SIZE) : this._freeBuffer;
    }

    public final T appendCompletedChunk(T fullChunk, int fullChunkLength) {
        Node<T> next = new Node(fullChunk, fullChunkLength);
        if (this._bufferHead == null) {
            this._bufferTail = next;
            this._bufferHead = next;
        } else {
            this._bufferTail.linkNext(next);
            this._bufferTail = next;
        }
        this._bufferedEntryCount += fullChunkLength;
        int nextLen = fullChunkLength;
        if (nextLen < SMALL_CHUNK_SIZE) {
            nextLen += nextLen;
        } else {
            nextLen += nextLen >> 2;
        }
        return _constructArray(nextLen);
    }

    public T completeAndClearBuffer(T lastChunk, int lastChunkEntries) {
        int totalSize = lastChunkEntries + this._bufferedEntryCount;
        T resultArray = _constructArray(totalSize);
        int ptr = 0;
        for (Node<T> n = this._bufferHead; n != null; n = n.next()) {
            ptr = n.copyData(resultArray, ptr);
        }
        System.arraycopy(lastChunk, 0, resultArray, ptr, lastChunkEntries);
        ptr += lastChunkEntries;
        if (ptr == totalSize) {
            return resultArray;
        }
        throw new IllegalStateException("Should have gotten " + totalSize + " entries, got " + ptr);
    }

    protected void _reset() {
        if (this._bufferTail != null) {
            this._freeBuffer = this._bufferTail.getData();
        }
        this._bufferTail = null;
        this._bufferHead = null;
        this._bufferedEntryCount = 0;
    }
}
