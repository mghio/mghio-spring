package cn.mghio.core.type;

/**
 * @author mghio
 * @since 2020-11-26
 */
public interface ClassMetadata {

    String getClassName();

    boolean isInterface();

    boolean isAbstract();

    boolean isFinal();

    boolean hasSuperClass();

    String getSuperClassName();

    String[] getInterfaceNames();

}
