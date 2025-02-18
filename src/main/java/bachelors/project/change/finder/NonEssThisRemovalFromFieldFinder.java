package bachelors.project.change.finder;

import bachelors.project.ChangeFinder;

public class NonEssThisRemovalFromFieldFinder extends ChangeFinder {
//    @Override
//    public List<Action> findChanges(DiffData diffData) throws IOException {
//        List<Action> trivialThisKeywordChanges = new ArrayList<>();
//        EditScript actions = diffData.getEditScript();
//        for (Action action : actions) {
//            Optional<Action> parentDelete, siblingMove;
//            if (action instanceof Delete delete && delete.getNode().getType().name.equals("ThisExpression") &&
//                    delete.getNode().getParent().getType().name.equals("FieldAccess") && (parentDelete = actions.asList().stream().filter(act -> act instanceof Delete del && del.getNode() == delete.getNode().getParent()).findFirst()).isPresent() &&
//                    (siblingMove = actions.asList().stream().filter(act -> act instanceof Move mov && mov.getNode().getParent() == delete.getNode().getParent()).findFirst()).isPresent()) {
//                Pair<Integer, Integer> positionInBefore = PositionConverter.getLineAndColumn(diffData.getBeforeFilePath(), parentDelete.get().getNode().getPos());
//                String queryOutputBefore = JoernCient.executeQuery("cpg.fieldAccess.lineNumber(%d).columnNumber(%d).head.referencedMember.head.typeDecl.fullName"
//                        .formatted(positionInBefore.first, positionInBefore.second), diffData.getBeforeFilePath());
////                System.out.println(queryOutputBefore);
//                Tree equivalentNode = diffData.getMappings().getDstForSrc(siblingMove.get().getNode());
//                Pair<Integer, Integer> positionInAfter = PositionConverter.getLineAndColumn(diffData.getAfterFilePath(), equivalentNode.getPos());
//                String queryOutputAfter = JoernCient.executeQuery("cpg.fieldAccess.lineNumber(%d).columnNumber(%d).head.referencedMember.head.typeDecl.fullName"
//                        .formatted(positionInAfter.first, positionInAfter.second), diffData.getAfterFilePath());
////                System.out.println(queryOutputAfter);
//                if (queryOutputBefore.equals(queryOutputAfter)) {
//                    trivialThisKeywordChanges.addAll(List.of(delete, parentDelete.get(), siblingMove.get()));
//                }
//            }
//        }
//        return trivialThisKeywordChanges;
//
//    }
}
