# POC for [PMD#5293](https://github.com/pmd/pmd/issues/5293)

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
