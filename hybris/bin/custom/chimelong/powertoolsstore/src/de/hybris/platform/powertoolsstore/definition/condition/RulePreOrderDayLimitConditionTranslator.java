package de.hybris.platform.powertoolsstore.definition.condition;

import com.google.common.collect.Lists;
import de.hybris.platform.ruledefinitions.AmountOperator;
import de.hybris.platform.ruledefinitions.conditions.AbstractRuleConditionTranslator;
import de.hybris.platform.ruledefinitions.conditions.builders.IrConditions;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeRelConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrGroupConditionBuilder;
import de.hybris.platform.ruleengineservices.compiler.*;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;

import java.util.List;
import java.util.Map;

/**
 * Created on 14/05/2018.
 */
public class RulePreOrderDayLimitConditionTranslator extends AbstractRuleConditionTranslator {
    @Override
    public RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition) {
        Map<String, RuleParameterData> conditionParameters = condition.getParameters();
        RuleParameterData operatorParameter = conditionParameters.get("operator");
        RuleParameterData daysInAdvanceParameter = conditionParameters.get("daysInAdvance");

        if (this.verifyAllPresent(operatorParameter, daysInAdvanceParameter)) {
            AmountOperator operator = operatorParameter.getValue();
            return this.getPreOrderDayLimitConditions(context, operator, daysInAdvanceParameter.getValue());
        }

        return IrConditions.newIrRuleFalseCondition();
    }

    private RuleIrCondition getPreOrderDayLimitConditions(RuleCompilerContext context, AmountOperator operator, long daysInAdvance) {
        RuleIrGroupCondition irGroupCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).build();
        this.addPreOrderDayLimitConditions(context, operator, daysInAdvance, irGroupCondition);
        return irGroupCondition;
    }

    private void addPreOrderDayLimitConditions(RuleCompilerContext context, AmountOperator operator, long daysInAdvance, RuleIrGroupCondition irGroupCondition) {
        String orderEntryRaoVariable = context.generateVariable(OrderEntryRAO.class);
        String cartRaoVariable = context.generateVariable(CartRAO.class);
        List<RuleIrCondition> conditions = Lists.newArrayList();
        RuleIrGroupCondition irUseTimeGroupCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).withChildren(conditions).build();
        conditions.add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable).withAttribute("daysInAdvance").withOperator(RuleIrAttributeOperator.valueOf(operator.name())).withValue(daysInAdvance).build());
        conditions.add(RuleIrAttributeRelConditionBuilder.newAttributeRelationConditionFor(cartRaoVariable).withAttribute("entries").withOperator(RuleIrAttributeOperator.CONTAINS).withTargetVariable(orderEntryRaoVariable).build());
        irGroupCondition.getChildren().add(irUseTimeGroupCondition);
    }
}
