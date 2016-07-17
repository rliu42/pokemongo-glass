package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;

public class ReferenceType extends SimpleType {
    private static final long serialVersionUID = 1;
    protected final JavaType _referencedType;

    protected ReferenceType(Class<?> cls, JavaType refType, Object valueHandler, Object typeHandler, boolean asStatic) {
        super(cls, refType.hashCode(), valueHandler, typeHandler, asStatic);
        this._referencedType = refType;
    }

    public static ReferenceType construct(Class<?> cls, JavaType refType, Object valueHandler, Object typeHandler) {
        return new ReferenceType(cls, refType, null, null, false);
    }

    public ReferenceType withTypeHandler(Object h) {
        if (h == this._typeHandler) {
            return this;
        }
        return new ReferenceType(this._class, this._referencedType, this._valueHandler, h, this._asStatic);
    }

    public ReferenceType withContentTypeHandler(Object h) {
        return h == this._referencedType.getTypeHandler() ? this : new ReferenceType(this._class, this._referencedType.withTypeHandler(h), this._valueHandler, this._typeHandler, this._asStatic);
    }

    public ReferenceType withValueHandler(Object h) {
        if (h == this._valueHandler) {
            return this;
        }
        return new ReferenceType(this._class, this._referencedType, h, this._typeHandler, this._asStatic);
    }

    public ReferenceType withContentValueHandler(Object h) {
        return h == this._referencedType.getValueHandler() ? this : new ReferenceType(this._class, this._referencedType.withValueHandler(h), this._valueHandler, this._typeHandler, this._asStatic);
    }

    public ReferenceType withStaticTyping() {
        return this._asStatic ? this : new ReferenceType(this._class, this._referencedType.withStaticTyping(), this._valueHandler, this._typeHandler, true);
    }

    protected String buildCanonicalName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._class.getName());
        sb.append('<');
        sb.append(this._referencedType.toCanonical());
        return sb.toString();
    }

    protected JavaType _narrow(Class<?> subclass) {
        return new ReferenceType(subclass, this._referencedType, this._valueHandler, this._typeHandler, this._asStatic);
    }

    public JavaType getReferencedType() {
        return this._referencedType;
    }

    public boolean isReferenceType() {
        return true;
    }

    public int containedTypeCount() {
        return 1;
    }

    public JavaType containedType(int index) {
        return index == 0 ? this._referencedType : null;
    }

    public String containedTypeName(int index) {
        return index == 0 ? "T" : null;
    }

    public Class<?> getParameterSource() {
        return this._class;
    }

    public StringBuilder getErasedSignature(StringBuilder sb) {
        return TypeBase._classSignature(this._class, sb, true);
    }

    public StringBuilder getGenericSignature(StringBuilder sb) {
        TypeBase._classSignature(this._class, sb, false);
        sb.append('<');
        sb = this._referencedType.getGenericSignature(sb);
        sb.append(';');
        return sb;
    }

    public String toString() {
        return "[reference type, class " + buildCanonicalName() + '<' + this._referencedType + '>' + ']';
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        ReferenceType other = (ReferenceType) o;
        if (other._class == this._class) {
            return this._referencedType.equals(other._referencedType);
        }
        return false;
    }
}
