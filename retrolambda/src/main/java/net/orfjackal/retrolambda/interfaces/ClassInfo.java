// Copyright © 2013-2015 Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.interfaces;

import net.orfjackal.retrolambda.util.Flags;
import org.objectweb.asm.*;

import java.util.*;

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;

public class ClassInfo {

    public final ClassReader reader;
    private final int access;
    public final Type type;
    public final Type superclass;
    private final List<Type> interfaces = new ArrayList<>();
    private final List<MethodInfo> methods = new ArrayList<>();
    private Optional<Type> companionClass = Optional.empty();

    public ClassInfo() {
        this.reader = null;
        this.access = 0;
        this.type = null;
        this.superclass = null;
    }

    public ClassInfo(ClassReader cr) {
        this.reader = cr;
        this.access = cr.getAccess();
        this.type = Type.getObjectType(cr.getClassName());
        if (cr.getSuperName() == null) {
            this.superclass = null;
        } else {
            this.superclass = Type.getObjectType(cr.getSuperName());
        }
        for (String iface : cr.getInterfaces()) {
            this.interfaces.add(Type.getObjectType(iface));
        }
    }

    public List<Type> getInterfaces() {
        return Collections.unmodifiableList(interfaces);
    }

    public List<MethodInfo> getMethods() {
        return Collections.unmodifiableList(methods);
    }

    public void addMethod(MethodRef method, MethodKind kind) {
        methods.add(new MethodInfo(method.tag, method.getSignature(), Type.getObjectType(method.owner), kind));
    }

    public Optional<Type> getCompanionClass() {
        return companionClass;
    }

    public void enableCompanionClass() {
        this.companionClass = Optional.of(Type.getObjectType(type.getInternalName() + "$"));
    }

    public boolean isClass() {
        return !isInterface();
    }

    public boolean isInterface() {
        return Flags.hasFlag(access, ACC_INTERFACE);
    }
}
