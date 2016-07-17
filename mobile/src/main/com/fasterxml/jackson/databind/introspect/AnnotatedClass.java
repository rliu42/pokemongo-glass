package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector.MixInResolver;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class AnnotatedClass extends Annotated {
    private static final AnnotationMap[] NO_ANNOTATION_MAPS;
    protected final AnnotationIntrospector _annotationIntrospector;
    protected final Class<?> _class;
    protected AnnotationMap _classAnnotations;
    protected List<AnnotatedConstructor> _constructors;
    protected List<AnnotatedMethod> _creatorMethods;
    protected boolean _creatorsResolved;
    protected AnnotatedConstructor _defaultConstructor;
    protected List<AnnotatedField> _fields;
    protected AnnotatedMethodMap _memberMethods;
    protected final MixInResolver _mixInResolver;
    protected final Class<?> _primaryMixIn;
    protected final List<Class<?>> _superTypes;

    static {
        NO_ANNOTATION_MAPS = new AnnotationMap[0];
    }

    private AnnotatedClass(Class<?> cls, List<Class<?>> superTypes, AnnotationIntrospector aintr, MixInResolver mir, AnnotationMap classAnnotations) {
        this._creatorsResolved = false;
        this._class = cls;
        this._superTypes = superTypes;
        this._annotationIntrospector = aintr;
        this._mixInResolver = mir;
        this._primaryMixIn = this._mixInResolver == null ? null : this._mixInResolver.findMixInClassFor(this._class);
        this._classAnnotations = classAnnotations;
    }

    public AnnotatedClass withAnnotations(AnnotationMap ann) {
        return new AnnotatedClass(this._class, this._superTypes, this._annotationIntrospector, this._mixInResolver, ann);
    }

    public static AnnotatedClass construct(Class<?> cls, AnnotationIntrospector aintr, MixInResolver mir) {
        return new AnnotatedClass(cls, ClassUtil.findSuperTypes(cls, null), aintr, mir, null);
    }

    public static AnnotatedClass constructWithoutSuperTypes(Class<?> cls, AnnotationIntrospector aintr, MixInResolver mir) {
        return new AnnotatedClass(cls, Collections.emptyList(), aintr, mir, null);
    }

    public Class<?> getAnnotated() {
        return this._class;
    }

    public int getModifiers() {
        return this._class.getModifiers();
    }

    public String getName() {
        return this._class.getName();
    }

    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        if (this._classAnnotations == null) {
            resolveClassAnnotations();
        }
        return this._classAnnotations.get(acls);
    }

    public Type getGenericType() {
        return this._class;
    }

    public Class<?> getRawType() {
        return this._class;
    }

    public Iterable<Annotation> annotations() {
        if (this._classAnnotations == null) {
            resolveClassAnnotations();
        }
        return this._classAnnotations.annotations();
    }

    protected AnnotationMap getAllAnnotations() {
        if (this._classAnnotations == null) {
            resolveClassAnnotations();
        }
        return this._classAnnotations;
    }

    public Annotations getAnnotations() {
        if (this._classAnnotations == null) {
            resolveClassAnnotations();
        }
        return this._classAnnotations;
    }

    public boolean hasAnnotations() {
        if (this._classAnnotations == null) {
            resolveClassAnnotations();
        }
        return this._classAnnotations.size() > 0;
    }

    public AnnotatedConstructor getDefaultConstructor() {
        if (!this._creatorsResolved) {
            resolveCreators();
        }
        return this._defaultConstructor;
    }

    public List<AnnotatedConstructor> getConstructors() {
        if (!this._creatorsResolved) {
            resolveCreators();
        }
        return this._constructors;
    }

    public List<AnnotatedMethod> getStaticMethods() {
        if (!this._creatorsResolved) {
            resolveCreators();
        }
        return this._creatorMethods;
    }

    public Iterable<AnnotatedMethod> memberMethods() {
        if (this._memberMethods == null) {
            resolveMemberMethods();
        }
        return this._memberMethods;
    }

    public int getMemberMethodCount() {
        if (this._memberMethods == null) {
            resolveMemberMethods();
        }
        return this._memberMethods.size();
    }

    public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes) {
        if (this._memberMethods == null) {
            resolveMemberMethods();
        }
        return this._memberMethods.find(name, paramTypes);
    }

    public int getFieldCount() {
        if (this._fields == null) {
            resolveFields();
        }
        return this._fields.size();
    }

    public Iterable<AnnotatedField> fields() {
        if (this._fields == null) {
            resolveFields();
        }
        return this._fields;
    }

    private void resolveClassAnnotations() {
        this._classAnnotations = new AnnotationMap();
        if (this._annotationIntrospector != null) {
            if (this._primaryMixIn != null) {
                _addClassMixIns(this._classAnnotations, this._class, this._primaryMixIn);
            }
            _addAnnotationsIfNotPresent(this._classAnnotations, this._class.getDeclaredAnnotations());
            for (Class<?> cls : this._superTypes) {
                _addClassMixIns(this._classAnnotations, cls);
                _addAnnotationsIfNotPresent(this._classAnnotations, cls.getDeclaredAnnotations());
            }
            _addClassMixIns(this._classAnnotations, Object.class);
        }
    }

    private void resolveCreators() {
        int i;
        List<AnnotatedConstructor> constructors = null;
        Constructor<?>[] declaredCtors = this._class.getDeclaredConstructors();
        for (Constructor<?> ctor : declaredCtors) {
            if (ctor.getParameterTypes().length == 0) {
                this._defaultConstructor = _constructConstructor(ctor, true);
            } else {
                if (constructors == null) {
                    constructors = new ArrayList(Math.max(10, declaredCtors.length));
                }
                constructors.add(_constructConstructor(ctor, false));
            }
        }
        if (constructors == null) {
            this._constructors = Collections.emptyList();
        } else {
            this._constructors = constructors;
        }
        if (!(this._primaryMixIn == null || (this._defaultConstructor == null && this._constructors.isEmpty()))) {
            _addConstructorMixIns(this._primaryMixIn);
        }
        if (this._annotationIntrospector != null) {
            if (this._defaultConstructor != null && this._annotationIntrospector.hasIgnoreMarker(this._defaultConstructor)) {
                this._defaultConstructor = null;
            }
            if (this._constructors != null) {
                i = this._constructors.size();
                while (true) {
                    i--;
                    if (i < 0) {
                        break;
                    } else if (this._annotationIntrospector.hasIgnoreMarker((AnnotatedMember) this._constructors.get(i))) {
                        this._constructors.remove(i);
                    }
                }
            }
        }
        List<AnnotatedMethod> creatorMethods = null;
        for (Method m : _findClassMethods(this._class)) {
            if (Modifier.isStatic(m.getModifiers())) {
                if (creatorMethods == null) {
                    creatorMethods = new ArrayList(8);
                }
                creatorMethods.add(_constructCreatorMethod(m));
            }
        }
        if (creatorMethods != null) {
            this._creatorMethods = creatorMethods;
            if (this._primaryMixIn != null) {
                _addFactoryMixIns(this._primaryMixIn);
            }
            if (this._annotationIntrospector != null) {
                i = this._creatorMethods.size();
                while (true) {
                    i--;
                    if (i < 0) {
                        break;
                    } else if (this._annotationIntrospector.hasIgnoreMarker((AnnotatedMember) this._creatorMethods.get(i))) {
                        this._creatorMethods.remove(i);
                    }
                }
            }
        } else {
            this._creatorMethods = Collections.emptyList();
        }
        this._creatorsResolved = true;
    }

    private void resolveMemberMethods() {
        this._memberMethods = new AnnotatedMethodMap();
        AnnotatedMethodMap mixins = new AnnotatedMethodMap();
        _addMemberMethods(this._class, this._memberMethods, this._primaryMixIn, mixins);
        for (Class<?> cls : this._superTypes) {
            _addMemberMethods(cls, this._memberMethods, this._mixInResolver == null ? null : this._mixInResolver.findMixInClassFor(cls), mixins);
        }
        if (this._mixInResolver != null) {
            Class<?> mixin = this._mixInResolver.findMixInClassFor(Object.class);
            if (mixin != null) {
                _addMethodMixIns(this._class, this._memberMethods, mixin, mixins);
            }
        }
        if (this._annotationIntrospector != null && !mixins.isEmpty()) {
            Iterator<AnnotatedMethod> it = mixins.iterator();
            while (it.hasNext()) {
                AnnotatedMethod mixIn = (AnnotatedMethod) it.next();
                try {
                    Method m = Object.class.getDeclaredMethod(mixIn.getName(), mixIn.getRawParameterTypes());
                    if (m != null) {
                        AnnotatedMethod am = _constructMethod(m);
                        _addMixOvers(mixIn.getAnnotated(), am, false);
                        this._memberMethods.add(am);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private void resolveFields() {
        Map<String, AnnotatedField> foundFields = _findFields(this._class, null);
        if (foundFields == null || foundFields.size() == 0) {
            this._fields = Collections.emptyList();
            return;
        }
        this._fields = new ArrayList(foundFields.size());
        this._fields.addAll(foundFields.values());
    }

    protected void _addClassMixIns(AnnotationMap annotations, Class<?> toMask) {
        if (this._mixInResolver != null) {
            _addClassMixIns(annotations, toMask, this._mixInResolver.findMixInClassFor(toMask));
        }
    }

    protected void _addClassMixIns(AnnotationMap annotations, Class<?> toMask, Class<?> mixin) {
        if (mixin != null) {
            _addAnnotationsIfNotPresent(annotations, mixin.getDeclaredAnnotations());
            for (Class<?> parent : ClassUtil.findSuperTypes(mixin, toMask)) {
                _addAnnotationsIfNotPresent(annotations, parent.getDeclaredAnnotations());
            }
        }
    }

    protected void _addConstructorMixIns(Class<?> mixin) {
        MemberKey[] ctorKeys = null;
        int ctorCount = this._constructors == null ? 0 : this._constructors.size();
        for (Constructor ctor : mixin.getDeclaredConstructors()) {
            if (ctor.getParameterTypes().length != 0) {
                int i;
                if (ctorKeys == null) {
                    ctorKeys = new MemberKey[ctorCount];
                    for (i = 0; i < ctorCount; i++) {
                        ctorKeys[i] = new MemberKey(((AnnotatedConstructor) this._constructors.get(i)).getAnnotated());
                    }
                }
                MemberKey key = new MemberKey(ctor);
                for (i = 0; i < ctorCount; i++) {
                    if (key.equals(ctorKeys[i])) {
                        _addMixOvers(ctor, (AnnotatedConstructor) this._constructors.get(i), true);
                        break;
                    }
                }
            } else if (this._defaultConstructor != null) {
                _addMixOvers(ctor, this._defaultConstructor, false);
            }
        }
    }

    protected void _addFactoryMixIns(Class<?> mixin) {
        MemberKey[] methodKeys = null;
        int methodCount = this._creatorMethods.size();
        for (Method m : mixin.getDeclaredMethods()) {
            if (Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length != 0) {
                int i;
                if (methodKeys == null) {
                    methodKeys = new MemberKey[methodCount];
                    for (i = 0; i < methodCount; i++) {
                        methodKeys[i] = new MemberKey(((AnnotatedMethod) this._creatorMethods.get(i)).getAnnotated());
                    }
                }
                MemberKey key = new MemberKey(m);
                for (i = 0; i < methodCount; i++) {
                    if (key.equals(methodKeys[i])) {
                        _addMixOvers(m, (AnnotatedMethod) this._creatorMethods.get(i), true);
                        break;
                    }
                }
            }
        }
    }

    protected void _addMemberMethods(Class<?> cls, AnnotatedMethodMap methods, Class<?> mixInCls, AnnotatedMethodMap mixIns) {
        if (mixInCls != null) {
            _addMethodMixIns(cls, methods, mixInCls, mixIns);
        }
        if (cls != null) {
            for (Method m : _findClassMethods(cls)) {
                if (_isIncludableMemberMethod(m)) {
                    AnnotatedMethod old = methods.find(m);
                    if (old == null) {
                        AnnotatedMethod newM = _constructMethod(m);
                        methods.add(newM);
                        old = mixIns.remove(m);
                        if (old != null) {
                            _addMixOvers(old.getAnnotated(), newM, false);
                        }
                    } else {
                        _addMixUnders(m, old);
                        if (old.getDeclaringClass().isInterface() && !m.getDeclaringClass().isInterface()) {
                            methods.add(old.withMethod(m));
                        }
                    }
                }
            }
        }
    }

    protected void _addMethodMixIns(Class<?> targetClass, AnnotatedMethodMap methods, Class<?> mixInCls, AnnotatedMethodMap mixIns) {
        List<Class<?>> parents = new ArrayList();
        parents.add(mixInCls);
        ClassUtil.findSuperTypes(mixInCls, targetClass, parents);
        for (Class<?> mixin : parents) {
            for (Method m : mixin.getDeclaredMethods()) {
                if (_isIncludableMemberMethod(m)) {
                    AnnotatedMethod am = methods.find(m);
                    if (am != null) {
                        _addMixUnders(m, am);
                    } else {
                        am = mixIns.find(m);
                        if (am != null) {
                            _addMixUnders(m, am);
                        } else {
                            mixIns.add(_constructMethod(m));
                        }
                    }
                }
            }
        }
    }

    protected Map<String, AnnotatedField> _findFields(Class<?> c, Map<String, AnnotatedField> fields) {
        Class<?> parent = c.getSuperclass();
        if (parent != null) {
            fields = _findFields(parent, fields);
            for (Field f : c.getDeclaredFields()) {
                if (_isIncludableField(f)) {
                    if (fields == null) {
                        fields = new LinkedHashMap();
                    }
                    fields.put(f.getName(), _constructField(f));
                }
            }
            if (this._mixInResolver != null) {
                Class<?> mixin = this._mixInResolver.findMixInClassFor(c);
                if (mixin != null) {
                    _addFieldMixIns(parent, mixin, fields);
                }
            }
        }
        return fields;
    }

    protected void _addFieldMixIns(Class<?> targetClass, Class<?> mixInCls, Map<String, AnnotatedField> fields) {
        List<Class<?>> parents = new ArrayList();
        parents.add(mixInCls);
        ClassUtil.findSuperTypes(mixInCls, targetClass, parents);
        for (Class<?> mixin : parents) {
            for (Field mixinField : mixin.getDeclaredFields()) {
                if (_isIncludableField(mixinField)) {
                    AnnotatedField maskedField = (AnnotatedField) fields.get(mixinField.getName());
                    if (maskedField != null) {
                        _addOrOverrideAnnotations(maskedField, mixinField.getDeclaredAnnotations());
                    }
                }
            }
        }
    }

    protected AnnotatedMethod _constructMethod(Method m) {
        if (this._annotationIntrospector == null) {
            return new AnnotatedMethod(this, m, _emptyAnnotationMap(), null);
        }
        return new AnnotatedMethod(this, m, _collectRelevantAnnotations(m.getDeclaredAnnotations()), null);
    }

    protected AnnotatedConstructor _constructConstructor(Constructor<?> ctor, boolean defaultCtor) {
        if (this._annotationIntrospector == null) {
            return new AnnotatedConstructor(this, ctor, _emptyAnnotationMap(), _emptyAnnotationMaps(ctor.getParameterTypes().length));
        }
        if (defaultCtor) {
            return new AnnotatedConstructor(this, ctor, _collectRelevantAnnotations(ctor.getDeclaredAnnotations()), null);
        }
        Annotation[][] paramAnns = ctor.getParameterAnnotations();
        int paramCount = ctor.getParameterTypes().length;
        AnnotationMap[] resolvedAnnotations = null;
        if (paramCount != paramAnns.length) {
            Class<?> dc = ctor.getDeclaringClass();
            Annotation[][] old;
            if (dc.isEnum() && paramCount == paramAnns.length + 2) {
                old = paramAnns;
                paramAnns = new Annotation[(old.length + 2)][];
                System.arraycopy(old, 0, paramAnns, 2, old.length);
                resolvedAnnotations = _collectRelevantAnnotations(paramAnns);
            } else if (dc.isMemberClass() && paramCount == paramAnns.length + 1) {
                old = paramAnns;
                paramAnns = new Annotation[(old.length + 1)][];
                System.arraycopy(old, 0, paramAnns, 1, old.length);
                resolvedAnnotations = _collectRelevantAnnotations(paramAnns);
            }
            if (resolvedAnnotations == null) {
                throw new IllegalStateException("Internal error: constructor for " + ctor.getDeclaringClass().getName() + " has mismatch: " + paramCount + " parameters; " + paramAnns.length + " sets of annotations");
            }
        }
        resolvedAnnotations = _collectRelevantAnnotations(paramAnns);
        return new AnnotatedConstructor(this, ctor, _collectRelevantAnnotations(ctor.getDeclaredAnnotations()), resolvedAnnotations);
    }

    protected AnnotatedMethod _constructCreatorMethod(Method m) {
        if (this._annotationIntrospector == null) {
            return new AnnotatedMethod(this, m, _emptyAnnotationMap(), _emptyAnnotationMaps(m.getParameterTypes().length));
        }
        return new AnnotatedMethod(this, m, _collectRelevantAnnotations(m.getDeclaredAnnotations()), _collectRelevantAnnotations(m.getParameterAnnotations()));
    }

    protected AnnotatedField _constructField(Field f) {
        if (this._annotationIntrospector == null) {
            return new AnnotatedField(this, f, _emptyAnnotationMap());
        }
        return new AnnotatedField(this, f, _collectRelevantAnnotations(f.getDeclaredAnnotations()));
    }

    private AnnotationMap _emptyAnnotationMap() {
        return new AnnotationMap();
    }

    private AnnotationMap[] _emptyAnnotationMaps(int count) {
        if (count == 0) {
            return NO_ANNOTATION_MAPS;
        }
        AnnotationMap[] maps = new AnnotationMap[count];
        for (int i = 0; i < count; i++) {
            maps[i] = _emptyAnnotationMap();
        }
        return maps;
    }

    protected boolean _isIncludableMemberMethod(Method m) {
        if (Modifier.isStatic(m.getModifiers()) || m.isSynthetic() || m.isBridge() || m.getParameterTypes().length > 2) {
            return false;
        }
        return true;
    }

    private boolean _isIncludableField(Field f) {
        if (f.isSynthetic() || Modifier.isStatic(f.getModifiers())) {
            return false;
        }
        return true;
    }

    protected AnnotationMap[] _collectRelevantAnnotations(Annotation[][] anns) {
        int len = anns.length;
        AnnotationMap[] result = new AnnotationMap[len];
        for (int i = 0; i < len; i++) {
            result[i] = _collectRelevantAnnotations(anns[i]);
        }
        return result;
    }

    protected AnnotationMap _collectRelevantAnnotations(Annotation[] anns) {
        return _addAnnotationsIfNotPresent(new AnnotationMap(), anns);
    }

    private AnnotationMap _addAnnotationsIfNotPresent(AnnotationMap result, Annotation[] anns) {
        if (anns != null) {
            List<Annotation> fromBundles = null;
            for (Annotation ann : anns) {
                if (result.addIfNotPresent(ann) && _isAnnotationBundle(ann)) {
                    fromBundles = _addFromBundle(ann, fromBundles);
                }
            }
            if (fromBundles != null) {
                _addAnnotationsIfNotPresent(result, (Annotation[]) fromBundles.toArray(new Annotation[fromBundles.size()]));
            }
        }
        return result;
    }

    private List<Annotation> _addFromBundle(Annotation bundle, List<Annotation> result) {
        for (Annotation a : bundle.annotationType().getDeclaredAnnotations()) {
            if (!((a instanceof Target) || (a instanceof Retention))) {
                if (result == null) {
                    result = new ArrayList();
                }
                result.add(a);
            }
        }
        return result;
    }

    private void _addAnnotationsIfNotPresent(AnnotatedMember target, Annotation[] anns) {
        if (anns != null) {
            List<Annotation> fromBundles = null;
            for (Annotation ann : anns) {
                if (target.addIfNotPresent(ann) && _isAnnotationBundle(ann)) {
                    fromBundles = _addFromBundle(ann, fromBundles);
                }
            }
            if (fromBundles != null) {
                _addAnnotationsIfNotPresent(target, (Annotation[]) fromBundles.toArray(new Annotation[fromBundles.size()]));
            }
        }
    }

    private void _addOrOverrideAnnotations(AnnotatedMember target, Annotation[] anns) {
        if (anns != null) {
            List<Annotation> fromBundles = null;
            for (Annotation ann : anns) {
                if (target.addOrOverride(ann) && _isAnnotationBundle(ann)) {
                    fromBundles = _addFromBundle(ann, fromBundles);
                }
            }
            if (fromBundles != null) {
                _addOrOverrideAnnotations(target, (Annotation[]) fromBundles.toArray(new Annotation[fromBundles.size()]));
            }
        }
    }

    protected void _addMixOvers(Constructor<?> mixin, AnnotatedConstructor target, boolean addParamAnnotations) {
        _addOrOverrideAnnotations(target, mixin.getDeclaredAnnotations());
        if (addParamAnnotations) {
            Annotation[][] pa = mixin.getParameterAnnotations();
            int len = pa.length;
            for (int i = 0; i < len; i++) {
                for (Annotation a : pa[i]) {
                    target.addOrOverrideParam(i, a);
                }
            }
        }
    }

    protected void _addMixOvers(Method mixin, AnnotatedMethod target, boolean addParamAnnotations) {
        _addOrOverrideAnnotations(target, mixin.getDeclaredAnnotations());
        if (addParamAnnotations) {
            Annotation[][] pa = mixin.getParameterAnnotations();
            int len = pa.length;
            for (int i = 0; i < len; i++) {
                for (Annotation a : pa[i]) {
                    target.addOrOverrideParam(i, a);
                }
            }
        }
    }

    protected void _addMixUnders(Method src, AnnotatedMethod target) {
        _addAnnotationsIfNotPresent((AnnotatedMember) target, src.getDeclaredAnnotations());
    }

    private final boolean _isAnnotationBundle(Annotation ann) {
        return this._annotationIntrospector != null && this._annotationIntrospector.isAnnotationBundle(ann);
    }

    protected Method[] _findClassMethods(Class<?> cls) {
        try {
            return cls.getDeclaredMethods();
        } catch (NoClassDefFoundError ex) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                throw ex;
            }
            try {
                return loader.loadClass(cls.getName()).getDeclaredMethods();
            } catch (ClassNotFoundException e) {
                throw ex;
            }
        }
    }

    public String toString() {
        return "[AnnotedClass " + this._class.getName() + "]";
    }

    public int hashCode() {
        return this._class.getName().hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        if (((AnnotatedClass) o)._class != this._class) {
            return false;
        }
        return true;
    }
}
