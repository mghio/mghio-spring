package cn.mghio.core.type.classreading;

import cn.mghio.core.type.ClassMetadata;
import cn.mghio.utils.ClassUtils;
import cn.mghio.utils.StringUtils;
import lombok.Getter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author mghio
 * @since 2020-11-25
 */
public class ClassMetadataReadingVisitor extends ClassVisitor implements ClassMetadata {

    private String className;

    private Boolean isInterface;

    private Boolean isAbstract;

    private Boolean isFinal;

    private String superClassName;

    private String[] interfaceNames;

    public ClassMetadataReadingVisitor() {
        super(Opcodes.ASM7);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = ClassUtils.convertResourcePathToClassName(name);
        this.isInterface = ((access & Opcodes.ACC_INTERFACE) != 0);
        this.isAbstract = ((access & Opcodes.ACC_ABSTRACT) != 0);
        this.isFinal = ((access & Opcodes.ACC_FINAL) != 0);
        if (superName != null) {
            this.superClassName = ClassUtils.convertResourcePathToClassName(superName);
        }
        if (interfaces != null) {
            this.interfaceNames = new String[interfaces.length];
            System.arraycopy(interfaces, 0, this.interfaceNames, 0, interfaces.length);
        }
    }

    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public boolean isInterface() {
        return this.isInterface;
    }

    @Override
    public boolean isAbstract() {
        return this.isAbstract;
    }

    @Override
    public boolean isFinal() {
        return this.isFinal;
    }

    @Override
    public boolean hasSuperClass() {
        return StringUtils.hasText(this.superClassName);
    }

    @Override
    public String getSuperClassName() {
        return this.superClassName;
    }

    @Override
    public String[] getInterfaceNames() {
        return this.interfaceNames;
    }
}
