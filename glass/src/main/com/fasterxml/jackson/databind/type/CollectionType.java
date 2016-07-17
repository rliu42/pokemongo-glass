package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import java.util.Collection;

public final class CollectionType extends CollectionLikeType {
    private static final long serialVersionUID = 1;

    private CollectionType(Class<?> collT, JavaType elemT, Object valueHandler, Object typeHandler, boolean asStatic) {
        super(collT, elemT, valueHandler, typeHandler, asStatic);
    }

    protected JavaType _narrow(Class<?> subclass) {
        return new CollectionType(subclass, this._elementType, null, null, this._asStatic);
    }

    public JavaType narrowContentsBy(Class<?> contentClass) {
        return contentClass == this._elementType.getRawClass() ? this : new CollectionType(this._class, this._elementType.narrowBy(contentClass), this._valueHandler, this._typeHandler, this._asStatic);
    }

    public JavaType widenContentsBy(Class<?> contentClass) {
        return contentClass == this._elementType.getRawClass() ? this : new CollectionType(this._class, this._elementType.widenBy(contentClass), this._valueHandler, this._typeHandler, this._asStatic);
    }

    public static CollectionType construct(Class<?> rawType, JavaType elemT) {
        return new CollectionType(rawType, elemT, null, null, false);
    }

    public CollectionType withTypeHandler(Object h) {
        return new CollectionType(this._class, this._elementType, this._valueHandler, h, this._asStatic);
    }

    public CollectionType withContentTypeHandler(Object h) {
        return new CollectionType(this._class, this._elementType.withTypeHandler(h), this._valueHandler, this._typeHandler, this._asStatic);
    }

    public CollectionType withValueHandler(Object h) {
        return new CollectionType(this._class, this._elementType, h, this._typeHandler, this._asStatic);
    }

    public CollectionType withContentValueHandler(Object h) {
        return new CollectionType(this._class, this._elementType.withValueHandler(h), this._valueHandler, this._typeHandler, this._asStatic);
    }

    public CollectionType withStaticTyping() {
        return this._asStatic ? this : new CollectionType(this._class, this._elementType.withStaticTyping(), this._valueHandler, this._typeHandler, true);
    }

    public Class<?> getParameterSource() {
        return Collection.class;
    }

    public String toString() {
        return "[collection type; class " + this._class.getName() + ", contains " + this._elementType + "]";
    }
}
