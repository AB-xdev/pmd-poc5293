# POC for [PMD#5293](https://github.com/pmd/pmd/issues/5293)

> [!NOTE]
> Problem from the issue can't be reproduced here (reliably). This is likely because not all code from the original (private) project is present.
> 
> If you try to reproduce it you have to likely halt and resume the threads manually so that they get into a stuck state.
>
> To control amount of spawned PMD threads use ``-XX:ActiveProcessorCount=<number_of_threads>``

Problems should look like this:

```
Format:
<net.sourceforge.pmd.lang.java.symbols.internal.asm.ClassStub.getTypeParameters (ClassStub.java:314)> (Signature: <at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase.getTypeParams (GenericSigBase.java:92)>)

Thread #1
ENTRY -> ....ui.components.user.UserActivityGridHelper
-> com.vaadin.flow.component.grid.Grid$Column (Signature: <T:Ljava/lang/Object;>Lcom/vaadin/flow/component/grid/AbstractColumn<Lcom/vaadin/flow/component/grid/Grid$Column<TT;>;>;)
-> com.vaadin.flow.component.grid.Grid (Signature: <T:Ljava/lang/Object;>Lcom/vaadin/flow/component/Component;Lcom/vaadin/flow/component/HasStyle;Lcom/vaadin/flow/component/HasSize;Lcom/vaadin/flow/component/Foc...)


Thread #4
ENTRY -> ....ui.components.compareview.AbstractCompareView
-> com.vaadin.flow.component.grid.Grid (Signature: <T:Ljava/lang/Object;>Lcom/vaadin/flow/component/Component;Lcom/vaadin/flow/component/HasStyle;Lcom/vaadin/flow/component/HasSize;Lcom/vaadin/flow/component/Foc...)
-> com.vaadin.flow.component.grid.GridSortOrder (Signature: <T:Ljava/lang/Object;>Lcom/vaadin/flow/data/provider/SortOrder<Lcom/vaadin/flow/component/grid/Grid$Column<TT;>;>;)
-> com.vaadin.flow.component.grid.Grid$Column (Signature: <T:Ljava/lang/Object;>Lcom/vaadin/flow/component/grid/AbstractColumn<Lcom/vaadin/flow/component/grid/Grid$Column<TT;>;>;)
```

resolved from Threaddump deadlock:
```
Found one Java-level deadlock:
=============================
"PmdThread 1":
  waiting to lock monitor 0x00000284a5197250 (object 0x0000000419a8b2a0, a net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase$1),
  which is held by "PmdThread 4"

"PmdThread 4":
  waiting to lock monitor 0x00000284a1b72aa0 (object 0x0000000419b2df48, a net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase$1),
  which is held by "PmdThread 1"

Java stack information for the threads listed above:
===================================================
"PmdThread 1":
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ParseLock.getFinalStatus(ParseLock.java:28)
        - waiting to lock <0x0000000419a8b2a0> (a net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase$1)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ParseLock.ensureParsed(ParseLock.java:22)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase.ensureParsed(GenericSigBase.java:76)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase.getTypeParams(GenericSigBase.java:92)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ClassStub.getTypeParameters(ClassStub.java:314)
        at net.sourceforge.pmd.lang.java.symbols.JTypeParameterOwnerSymbol.getLexicalScope(JTypeParameterOwnerSymbol.java:42)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ClassStub.getLexicalScope(ClassStub.java:326)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase.getEnclosingTypeParams(GenericSigBase.java:72)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.SignatureParser.typeParamsWrapper(SignatureParser.java:88)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.SignatureParser.parseClassSignature(SignatureParser.java:56)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase$LazyClassSignature.doParse(GenericSigBase.java:155)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase$1.doParse(GenericSigBase.java:54)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ParseLock.getFinalStatus(ParseLock.java:33)
        - locked <0x0000000419b2df48> (a net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase$1)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ParseLock.ensureParsed(ParseLock.java:22)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase.ensureParsed(GenericSigBase.java:76)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase.getTypeParams(GenericSigBase.java:92)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ClassStub.getTypeParameters(ClassStub.java:314)
        at net.sourceforge.pmd.lang.java.symbols.JTypeParameterOwnerSymbol.getTypeParameterCount(JTypeParameterOwnerSymbol.java:47)
        at net.sourceforge.pmd.lang.java.ast.AstDisambiguationPass$DisambigVisitor.postProcess(AstDisambiguationPass.java:219)
        at net.sourceforge.pmd.lang.java.ast.AstDisambiguationPass$DisambigVisitor.visit(AstDisambiguationPass.java:164)
        at net.sourceforge.pmd.lang.java.ast.AstDisambiguationPass$DisambigVisitor.visit(AstDisambiguationPass.java:80)
        at net.sourceforge.pmd.lang.java.ast.ASTClassType.acceptVisitor(ASTClassType.java:157)
        at net.sourceforge.pmd.lang.java.ast.AbstractJavaNode.acceptVisitor(AbstractJavaNode.java:38)
        at net.sourceforge.pmd.lang.java.ast.AstDisambiguationPass.lambda$disambigWithCtx$0(AstDisambiguationPass.java:58)
        at net.sourceforge.pmd.lang.java.ast.AstDisambiguationPass$$Lambda/0x000002845d606558.accept(Unknown Source)
        at net.sourceforge.pmd.lang.ast.internal.SingletonNodeStream.forEach(SingletonNodeStream.java:87)
        at net.sourceforge.pmd.lang.java.ast.AstDisambiguationPass.disambigWithCtx(AstDisambiguationPass.java:58)
        at net.sourceforge.pmd.lang.java.ast.InternalApiBridge.disambigWithCtx(InternalApiBridge.java:77)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymTableFactory.disambig(SymTableFactory.java:103)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver$MyVisitor.visit(SymbolTableResolver.java:188)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver$MyVisitor.visit(SymbolTableResolver.java:138)
        at net.sourceforge.pmd.lang.java.ast.ASTClassType.acceptVisitor(ASTClassType.java:157)
        at net.sourceforge.pmd.lang.java.ast.AbstractJavaNode.acceptVisitor(AbstractJavaNode.java:38)
        at net.sourceforge.pmd.lang.ast.AstVisitorBase.visitChildren(AstVisitorBase.java:31)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver$MyVisitor.setTopSymbolTableAndVisitAllChildren(SymbolTableResolver.java:741)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver$MyVisitor.visitMethodOrCtor(SymbolTableResolver.java:326)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver$MyVisitor.visitMethodOrCtor(SymbolTableResolver.java:138)
        at net.sourceforge.pmd.lang.java.ast.JavaVisitorBase.visit(JavaVisitorBase.java:39)
        at net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration.acceptVisitor(ASTMethodDeclaration.java:59)
        at net.sourceforge.pmd.lang.java.ast.AbstractJavaNode.acceptVisitor(AbstractJavaNode.java:38)
        at net.sourceforge.pmd.lang.ast.AstVisitorBase.visitChildren(AstVisitorBase.java:31)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver$MyVisitor.visitTypeDecl(SymbolTableResolver.java:296)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver$MyVisitor.visitTypeDecl(SymbolTableResolver.java:138)
        at net.sourceforge.pmd.lang.java.ast.JavaVisitorBase.visit(JavaVisitorBase.java:57)
        at net.sourceforge.pmd.lang.java.ast.ASTClassDeclaration.acceptVisitor(ASTClassDeclaration.java:38)
        at net.sourceforge.pmd.lang.java.ast.AbstractJavaNode.acceptVisitor(AbstractJavaNode.java:38)
        at net.sourceforge.pmd.lang.ast.AstVisitorBase.visitChildren(AstVisitorBase.java:31)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver$MyVisitor.visit(SymbolTableResolver.java:247)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver$MyVisitor.visit(SymbolTableResolver.java:138)
        at net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit.acceptVisitor(ASTCompilationUnit.java:109)
        at net.sourceforge.pmd.lang.java.ast.AbstractJavaNode.acceptVisitor(AbstractJavaNode.java:38)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver$MyVisitor.traverse(SymbolTableResolver.java:164)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver.traverse(SymbolTableResolver.java:101)
        at net.sourceforge.pmd.lang.java.internal.JavaAstProcessor.lambda$process$1(JavaAstProcessor.java:132)
        at net.sourceforge.pmd.lang.java.internal.JavaAstProcessor$$Lambda/0x000002845d5e9220.run(Unknown Source)
        at net.sourceforge.pmd.benchmark.TimeTracker.bench(TimeTracker.java:163)
        at net.sourceforge.pmd.lang.java.internal.JavaAstProcessor.process(JavaAstProcessor.java:132)
        at net.sourceforge.pmd.lang.java.internal.JavaAstProcessor.process(JavaAstProcessor.java:166)
        at net.sourceforge.pmd.lang.java.internal.JavaAstProcessor.process(JavaAstProcessor.java:150)
        at net.sourceforge.pmd.lang.java.ast.JavaParser.parseImpl(JavaParser.java:69)
        at net.sourceforge.pmd.lang.java.ast.JavaParser.parseImpl(JavaParser.java:25)
        at net.sourceforge.pmd.lang.ast.impl.javacc.JjtreeParserAdapter.parse(JjtreeParserAdapter.java:36)
        at net.sourceforge.pmd.lang.impl.PmdRunnable.parse(PmdRunnable.java:112)
        at net.sourceforge.pmd.lang.impl.PmdRunnable.processSource(PmdRunnable.java:132)
        at net.sourceforge.pmd.lang.impl.PmdRunnable.run(PmdRunnable.java:80)
        at java.util.concurrent.Executors$RunnableAdapter.call(java.base@21.0.5/Executors.java:572)
        at java.util.concurrent.FutureTask.run(java.base@21.0.5/FutureTask.java:317)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(java.base@21.0.5/ThreadPoolExecutor.java:1144)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(java.base@21.0.5/ThreadPoolExecutor.java:642)
        at java.lang.Thread.runWith(java.base@21.0.5/Thread.java:1596)
        at java.lang.Thread.run(java.base@21.0.5/Thread.java:1583)
"PmdThread 4":
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ParseLock.getFinalStatus(ParseLock.java:28)
        - waiting to lock <0x0000000419b2df48> (a net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase$1)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ParseLock.ensureParsed(ParseLock.java:22)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase.ensureParsed(GenericSigBase.java:76)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase.getTypeParams(GenericSigBase.java:92)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ClassStub.getTypeParameters(ClassStub.java:314)
        at net.sourceforge.pmd.lang.java.symbols.JTypeParameterOwnerSymbol.getTypeParameterCount(JTypeParameterOwnerSymbol.java:47)
        at net.sourceforge.pmd.lang.java.types.ClassTypeImpl.typeArgsAreOk(ClassTypeImpl.java:446)
        at net.sourceforge.pmd.lang.java.types.ClassTypeImpl.validateParams(ClassTypeImpl.java:420)
        at net.sourceforge.pmd.lang.java.types.ClassTypeImpl.<init>(ClassTypeImpl.java:69)
        at net.sourceforge.pmd.lang.java.types.ClassTypeImpl.<init>(ClassTypeImpl.java:64)
        at net.sourceforge.pmd.lang.java.types.TypeSystem.parameterise(TypeSystem.java:530)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser$TypeScanner.makeClassType(TypeSigParser.java:364)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser.classType(TypeSigParser.java:164)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser.typeSignature(TypeSigParser.java:148)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser.typeSignature(TypeSigParser.java:124)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser.typeArg(TypeSigParser.java:209)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser.typeArgsOpt(TypeSigParser.java:184)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser.classType(TypeSigParser.java:162)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser.classHeader(TypeSigParser.java:33)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.SignatureParser$$Lambda/0x000002845d607dc8.parse(Unknown Source)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.SignatureParser.parseFully(SignatureParser.java:100)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.SignatureParser.parseClassSignature(SignatureParser.java:57)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase$LazyClassSignature.doParse(GenericSigBase.java:155)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase$1.doParse(GenericSigBase.java:54)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ParseLock.getFinalStatus(ParseLock.java:33)
        - locked <0x0000000419b2e120> (a net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase$1)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ParseLock.ensureParsed(ParseLock.java:22)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase.ensureParsed(GenericSigBase.java:76)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase.getTypeParams(GenericSigBase.java:92)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ClassStub.getTypeParameters(ClassStub.java:314)
        at net.sourceforge.pmd.lang.java.symbols.JTypeParameterOwnerSymbol.getTypeParameterCount(JTypeParameterOwnerSymbol.java:47)
        at net.sourceforge.pmd.lang.java.types.ClassTypeImpl.typeArgsAreOk(ClassTypeImpl.java:446)
        at net.sourceforge.pmd.lang.java.types.ClassTypeImpl.validateParams(ClassTypeImpl.java:420)
        at net.sourceforge.pmd.lang.java.types.ClassTypeImpl.<init>(ClassTypeImpl.java:69)
        at net.sourceforge.pmd.lang.java.types.ClassTypeImpl.<init>(ClassTypeImpl.java:64)
        at net.sourceforge.pmd.lang.java.types.TypeSystem.parameterise(TypeSystem.java:530)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser$TypeScanner.makeClassType(TypeSigParser.java:364)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser.classType(TypeSigParser.java:164)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser.typeSignature(TypeSigParser.java:148)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser.typeSignature(TypeSigParser.java:124)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser.typeArg(TypeSigParser.java:209)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser.typeArgsOpt(TypeSigParser.java:184)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser.classType(TypeSigParser.java:162)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.TypeSigParser.classHeader(TypeSigParser.java:39)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.SignatureParser$$Lambda/0x000002845d607dc8.parse(Unknown Source)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.SignatureParser.parseFully(SignatureParser.java:100)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.SignatureParser.parseClassSignature(SignatureParser.java:57)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase$LazyClassSignature.doParse(GenericSigBase.java:155)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase$1.doParse(GenericSigBase.java:54)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ParseLock.getFinalStatus(ParseLock.java:33)
        - locked <0x0000000419a8b2a0> (a net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase$1)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ParseLock.ensureParsed(ParseLock.java:22)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase.ensureParsed(GenericSigBase.java:76)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.GenericSigBase.getTypeParams(GenericSigBase.java:92)
        at net.sourceforge.pmd.lang.java.symbols.internal.asm.ClassStub.getTypeParameters(ClassStub.java:314)
        at net.sourceforge.pmd.lang.java.symbols.JTypeParameterOwnerSymbol.getTypeParameterCount(JTypeParameterOwnerSymbol.java:47)
        at net.sourceforge.pmd.lang.java.ast.AstDisambiguationPass$DisambigVisitor.postProcess(AstDisambiguationPass.java:219)
        at net.sourceforge.pmd.lang.java.ast.AstDisambiguationPass$DisambigVisitor.visit(AstDisambiguationPass.java:189)
        at net.sourceforge.pmd.lang.java.ast.AstDisambiguationPass$DisambigVisitor.visit(AstDisambiguationPass.java:80)
        at net.sourceforge.pmd.lang.java.ast.ASTClassType.acceptVisitor(ASTClassType.java:157)
        at net.sourceforge.pmd.lang.java.ast.AbstractJavaNode.acceptVisitor(AbstractJavaNode.java:38)
        at net.sourceforge.pmd.lang.java.ast.AstDisambiguationPass.lambda$disambigWithCtx$0(AstDisambiguationPass.java:58)
        at net.sourceforge.pmd.lang.java.ast.AstDisambiguationPass$$Lambda/0x000002845d606558.accept(Unknown Source)
        at java.util.Iterator.forEachRemaining(java.base@21.0.5/Iterator.java:133)
        at net.sourceforge.pmd.lang.ast.internal.IteratorBasedNStream.forEach(IteratorBasedNStream.java:102)
        at net.sourceforge.pmd.lang.java.ast.AstDisambiguationPass.disambigWithCtx(AstDisambiguationPass.java:58)
        at net.sourceforge.pmd.lang.java.ast.InternalApiBridge.disambigWithCtx(InternalApiBridge.java:77)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymTableFactory.disambig(SymTableFactory.java:103)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver$MyVisitor.visitTypeDecl(SymbolTableResolver.java:293)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver$MyVisitor.visitTypeDecl(SymbolTableResolver.java:138)
        at net.sourceforge.pmd.lang.java.ast.JavaVisitorBase.visit(JavaVisitorBase.java:57)
        at net.sourceforge.pmd.lang.java.ast.ASTClassDeclaration.acceptVisitor(ASTClassDeclaration.java:38)
        at net.sourceforge.pmd.lang.java.ast.AbstractJavaNode.acceptVisitor(AbstractJavaNode.java:38)
        at net.sourceforge.pmd.lang.ast.AstVisitorBase.visitChildren(AstVisitorBase.java:31)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver$MyVisitor.visit(SymbolTableResolver.java:247)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver$MyVisitor.visit(SymbolTableResolver.java:138)
        at net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit.acceptVisitor(ASTCompilationUnit.java:109)
        at net.sourceforge.pmd.lang.java.ast.AbstractJavaNode.acceptVisitor(AbstractJavaNode.java:38)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver$MyVisitor.traverse(SymbolTableResolver.java:164)
        at net.sourceforge.pmd.lang.java.symbols.table.internal.SymbolTableResolver.traverse(SymbolTableResolver.java:101)
        at net.sourceforge.pmd.lang.java.internal.JavaAstProcessor.lambda$process$1(JavaAstProcessor.java:132)
        at net.sourceforge.pmd.lang.java.internal.JavaAstProcessor$$Lambda/0x000002845d5e9220.run(Unknown Source)
        at net.sourceforge.pmd.benchmark.TimeTracker.bench(TimeTracker.java:163)
        at net.sourceforge.pmd.lang.java.internal.JavaAstProcessor.process(JavaAstProcessor.java:132)
        at net.sourceforge.pmd.lang.java.internal.JavaAstProcessor.process(JavaAstProcessor.java:166)
        at net.sourceforge.pmd.lang.java.internal.JavaAstProcessor.process(JavaAstProcessor.java:150)
        at net.sourceforge.pmd.lang.java.ast.JavaParser.parseImpl(JavaParser.java:69)
        at net.sourceforge.pmd.lang.java.ast.JavaParser.parseImpl(JavaParser.java:25)
        at net.sourceforge.pmd.lang.ast.impl.javacc.JjtreeParserAdapter.parse(JjtreeParserAdapter.java:36)
        at net.sourceforge.pmd.lang.impl.PmdRunnable.parse(PmdRunnable.java:112)
        at net.sourceforge.pmd.lang.impl.PmdRunnable.processSource(PmdRunnable.java:132)
        at net.sourceforge.pmd.lang.impl.PmdRunnable.run(PmdRunnable.java:80)
        at java.util.concurrent.Executors$RunnableAdapter.call(java.base@21.0.5/Executors.java:572)
        at java.util.concurrent.FutureTask.run(java.base@21.0.5/FutureTask.java:317)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(java.base@21.0.5/ThreadPoolExecutor.java:1144)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(java.base@21.0.5/ThreadPoolExecutor.java:642)
        at java.lang.Thread.runWith(java.base@21.0.5/Thread.java:1596)
        at java.lang.Thread.run(java.base@21.0.5/Thread.java:1583)

Found 1 deadlock.
```
