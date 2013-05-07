/*
 * Copyright (C) Qatar Computing Research Institute, 2013.
 * All rights reserved.
 */

package qa.qcri.nadeef.core.operator;

import com.google.common.base.Preconditions;
import qa.qcri.nadeef.core.datamodel.*;
import qa.qcri.nadeef.tools.DBConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SourceDeserializer generates tuples for the rule. It also does the optimization
 * based on the input rule hints.
 */
public class SourceDeserializer extends Operator<Rule, Collection<TupleCollection>> {
    private DBConfig dbConfig;

    /**
     * Constructor.
     * @param plan Clean plan.
     */
    public SourceDeserializer(CleanPlan plan) {
        super(plan);
        dbConfig = plan.getSourceDBConfig();
    }

    /**
     * Execute the operator.
     *
     * @param rule input rule.
     * @return output object.
     */
    @Override
    public Collection<TupleCollection> execute(Rule rule) {
        Preconditions.checkNotNull(rule);

        List<String> tableNames = rule.getTableNames();
        List<TupleCollection> collections = new ArrayList<TupleCollection>();
        if (tableNames.size() == 2) {
            collections.add(new SQLTupleCollection(tableNames.get(0), dbConfig));
            collections.add(new SQLTupleCollection(tableNames.get(1), dbConfig));
        } else {
            collections.add(new SQLTupleCollection(tableNames.get(0), dbConfig));
        }

        return collections;
    }
}
