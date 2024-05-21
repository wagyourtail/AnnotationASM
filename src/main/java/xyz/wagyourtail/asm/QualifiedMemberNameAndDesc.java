package xyz.wagyourtail.asm;

import org.objectweb.asm.Type;

public class QualifiedMemberNameAndDesc {

    public final Type owner;
    public final String name;
    public final Type desc;

    public QualifiedMemberNameAndDesc(Type owner, String name, Type desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

}
