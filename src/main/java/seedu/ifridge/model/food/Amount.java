package seedu.ifridge.model.food;

import static java.util.Objects.requireNonNull;
import static seedu.ifridge.commons.util.AppUtil.checkArgument;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.ifridge.model.food.exceptions.InvalidAmountException;
import seedu.ifridge.model.food.exceptions.InvalidUnitException;

/**
 * Represents a Food's amount in the grocery list.
 */
public class Amount {
    public static final String MESSAGE_CONSTRAINTS = "Amounts should be of the format value unit "
            + "and adhere to the following constraints:\n"
            + "1. The value part should only contain digits and can have be decimal point or not.\n"
            + "2. This is followed by a unit that can have a space in between or not. \n"
            + "The unit must be one of the following: \n"
            + "    - lbs, kgs, g, pounds, oz, L, ml, units.";

    public static final String VALUE_BEFORE_DECIMAL = "(\\d*)";
    public static final String VALUE_AFTER_DECIMAL = "(\\d+)";

    public static final String UNIT_TYPE_WEIGHT = "Weight";
    public static final String UNIT_TYPE_VOLUME = "Volume";
    public static final String UNIT_TYPE_QUANTITY = "Quantity";

    public static final String UNIT_POUND = "lbs";
    public static final String UNIT_KILOGRAM = "kg";
    public static final String UNIT_GRAM = "g";
    public static final String UNIT_OUNCE = "oz";
    public static final String UNIT_LITRE = "L";
    public static final String UNIT_MILLILITRE = "ml";
    public static final String UNIT_QUANTITY = "units";
    public static final String UNIT = "(lbs?|g|kg|oz?|L|ml|units)+";
    public static final String VALIDATION_REGEX = VALUE_BEFORE_DECIMAL + "\\.?" + VALUE_AFTER_DECIMAL + "\\s*" + UNIT;

    public static final float GRAM_TO_KG = 0.001f;
    public static final float POUND_TO_KG = 0.453592f;
    public static final float OUNCE_TO_KG = 0.0283495f;
    public static final float MILLILITRE_TO_LITRE = 0.001f;

    public static final float KG_TO_GRAM = 1.0f / 0.001f;
    public static final float KG_TO_POUND = 1.0f / 0.453592f;
    public static final float KG_TO_OUNCE = 1.0f / 0.0283495f;
    public static final float LITRE_TO_MILLILITRE = 1.0f / 0.001f;

    public static final String MESSAGE_UNIT_DOES_NOT_MATCH = "Unit does not match with the existing items";
    public static final String MESSAGE_UNIT_TYPE_DOES_NOT_MATCH = "Unit type does not match with the other items";
    public static final String MESSAGE_INVALID_RESULTANT_AMOUNT = "Amount used should not exceed "
        + "amount left in the item.";


    private static Pattern p = Pattern.compile("(\\d*\\.?\\d+)(\\s*)((lbs?|g|kg|oz?|L|ml|units?)+)");
    private static Matcher m;

    public final String fullAmt;

    /**
     * Constructs a {@code Name}.
     *
     * @param amount A valid amount.
     */
    public Amount(String amount) {
        requireNonNull(amount);
        checkArgument(isValidAmount(amount), MESSAGE_CONSTRAINTS);
        fullAmt = amount;
    }

    /**
     * Tests whether an input amount is valid.
     *
     * @param test The input amount as a {@code String}/
     * @return true if the input amount is valid.
     */
    public static boolean isValidAmount(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Retrieves the numerical value of an {@code Amount} object, without the units
     *
     * @param amt The {@code Amount} object to get the value from.
     * @return The numerical value of the given Amount object.
     */
    public static float getValue(Amount amt) {
        m = p.matcher(amt.toString());
        String valueAsString = "";

        m.find();
        valueAsString = m.group(1);

        return Float.valueOf(valueAsString);
    }

    /**
     * Retrieves the unit of an {@code Amount} object, without the numerical value
     *
     * @param amt The {@code Amount} object to get the unit from.
     * @return The unit of the Amount object in String format.
     */
    public static String getUnit(Amount amt) {
        m = p.matcher(amt.toString());
        String unit = "";

        m.find();
        unit = m.group(3);

        return unit;
    }

    public static String getUnitType(Amount amt) {
        String unit = getUnit(amt);

        switch (unit) {
        case UNIT_POUND:
        case UNIT_GRAM:
        case UNIT_KILOGRAM:
        case UNIT_OUNCE:
            return UNIT_TYPE_WEIGHT;
        case UNIT_LITRE:
        case UNIT_MILLILITRE:
            return UNIT_TYPE_VOLUME;
        case UNIT_QUANTITY:
            return UNIT_TYPE_QUANTITY;
        default:
            return "Wrong amount input";
        }
    }

    /**
     * Retrieves the weight of the Amount object.
     *
     * @param amt The Amount object to get the weight from.
     * @return The weight of the given Amount object.
     */
    public static float getAmountInKg(Amount amt) {
        String unit = getUnit(amt);
        float value = getValue(amt);

        switch (unit) {
        case UNIT_KILOGRAM:
            return value;
        case UNIT_GRAM:
            return value * GRAM_TO_KG;
        case UNIT_POUND:
            return value * POUND_TO_KG;
        case UNIT_OUNCE:
            return value * OUNCE_TO_KG;
        default:
            return 0;
        }
    }

    public static float getAmountInG(Amount amt) {
        return getAmountInKg(amt) * KG_TO_GRAM;
    }

    public static float getAmountInPound(Amount amt) {
        return getAmountInKg(amt) * KG_TO_POUND;
    }

    public static float getAmountInOunce(Amount amt) {
        return getAmountInKg(amt) * KG_TO_OUNCE;
    }

    /**
     * Retrieves the volume of the Amount object.
     *
     * @param amt The Amount object to get the volume from.
     * @return The volume of the given Amount object.
     */
    public static float getAmountInLitre(Amount amt) {
        String unit = getUnit(amt);
        float value = getValue(amt);

        switch (unit) {
        case UNIT_LITRE:
            return value;
        case UNIT_MILLILITRE:
            return value * MILLILITRE_TO_LITRE;
        default:
            return 0;
        }
    }

    public static float getAmountInMillilitre(Amount amt) {
        return getAmountInLitre(amt) * LITRE_TO_MILLILITRE;
    }

    /**
     * Retrieves the number of units specified in the Amount object.
     *
     * @param amt The Amount object to get the number of units from.
     * @return The number of units specified in the given Amount object.
     */
    public static float getAmountInUnit(Amount amt) {
        String unit = getUnit(amt);
        float value = getValue(amt);

        return unit.equals(UNIT_QUANTITY) ? value : 0;
    }

    /**
     * Reduces the value of amount by the specified amount
     * @param other the Amount class to be reduced by
     * @return Returns Amount with its value deducted
     */
    public Amount reduceBy(Amount other) throws InvalidUnitException, InvalidAmountException {
        if (!hasSameAmountUnitType(this, other)) {
            throw new InvalidUnitException(MESSAGE_UNIT_TYPE_DOES_NOT_MATCH);
        }

        String thisUnit = Amount.getUnit(this);
        float resultantAmount;

        if (!hasSameAmountUnit(this, other)) {
            Amount convertedOther = this.convertAmount(other);
            resultantAmount = Amount.getValue(this) - Amount.getValue(convertedOther);
        } else {
            resultantAmount = Amount.getValue(this) - Amount.getValue(other);
        }
        if (resultantAmount < 0) {
            throw new InvalidAmountException(MESSAGE_INVALID_RESULTANT_AMOUNT);
        }

        // convert to int if it's a whole number
        if (resultantAmount == Math.round(resultantAmount)) {
            int wholeResultantAmount = Math.round(resultantAmount);
            return new Amount(wholeResultantAmount + thisUnit);
        } else {
            resultantAmount = Float.parseFloat(String.format("%.2f", resultantAmount));
            return new Amount(resultantAmount + thisUnit);
        }
    }

    /**
     * Checks if the provided amounts have the same unit.
     * @param amt The first amount to be checked.
     * @param other The second amount to be checked.
     * @throws InvalidUnitException If the units are not consistent.
     */
    public static boolean hasSameAmountUnit(Amount amt, Amount other) {
        return getUnit(amt).equalsIgnoreCase(getUnit(other));
    }

    /**
     * Checks if the provided amounts have the same unit type.
     * @param amt The first amount to be checked.
     * @param other The second amount to be checked.
     * @throws InvalidUnitException If the unit type are not consistent.
     */
    public static boolean hasSameAmountUnitType(Amount amt, Amount other) {
        return getUnitType(amt).equalsIgnoreCase(getUnitType(other));
    }

    /**
     * Convert amount argument based on the unit type of the caller Amount.
     * @param other The amount to be converted.
     * @return The amount with unit converted.
     */
    public Amount convertAmount(Amount other) {
        String unitType = getUnitType(this);
        switch (unitType) {
        case UNIT_TYPE_WEIGHT:
            return this.convertAmountByWeight(other);
        case UNIT_TYPE_VOLUME:
            return this.convertAmountByVolume(other);
        default:
            return null;
        }
    }

    /**
     * Convert amount argument based on the weight unit of the caller Amount.
     * @param other The amount to be converted.
     * @return The amount with unit converted.
     */
    public Amount convertAmountByWeight(Amount other) {
        String thisUnit = getUnit(this);
        float otherValue;

        switch (thisUnit) {
        case UNIT_KILOGRAM:
            otherValue = getAmountInKg(other);
            break;
        case UNIT_GRAM:
            otherValue = getAmountInG(other);
            break;
        case UNIT_POUND:
            otherValue = getAmountInPound(other);
            break;
        case UNIT_OUNCE:
            otherValue = getAmountInOunce(other);
            break;
        default:
            return null;
        }
        return new Amount(otherValue + thisUnit);
    }

    /**
     * Convert the amount argument based on the volume unit of the caller Amount.
     * @param other The amount to be converted.
     * @return The amount with unit converted.
     */
    public Amount convertAmountByVolume(Amount other) {
        String thisUnit = getUnit(this);
        float otherValue;

        switch (thisUnit) {
        case UNIT_LITRE:
            otherValue = getAmountInLitre(other);
            break;
        case UNIT_MILLILITRE:
            otherValue = getAmountInMillilitre(other);
            break;
        default:
            return null;
        }
        return new Amount(otherValue + thisUnit);
    }

    /**
     * Increases the value of amount by the specified amount
     * @param amt the Amount to be increased by
     * @return Returns Amount with its value increased
     */
    public Amount increaseBy(Amount amt) {
        float resultantAmount = Amount.getValue(this) + Amount.getValue(amt);
        String unit = Amount.getUnit(this);
        return new Amount(resultantAmount + unit);
    }

    @Override
    public String toString() {
        return fullAmt;
    }

    @Override
    public int hashCode() {
        return fullAmt.hashCode();
    }

}