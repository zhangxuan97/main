package seedu.address.logic.parser.wastelist;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_MONTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_MONTH;

import java.util.Optional;

import seedu.address.logic.commands.wastelist.ReportWasteCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.waste.WasteMonth;

/**
 * Parser to parse the start and end dates of a report command
 */
public class ReportWasteCommandParser implements Parser<ReportWasteCommand> {

    public static final int ONE_PERIOD = 11;

    /**
     * Parses {@code userInput} into a command and returns it.
     *
     * @param userInput
     * @throws ParseException if {@code userInput} does not conform the expected format
     */
    @Override
    public ReportWasteCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(userInput, PREFIX_START_MONTH, PREFIX_END_MONTH);

        Optional<String> startWmString = argMultimap.getValue(PREFIX_START_MONTH);
        Optional<String> endWmString = argMultimap.getValue(PREFIX_END_MONTH);
        boolean startMonthGiven = startWmString.isPresent();
        boolean endMonthGiven = endWmString.isPresent();

        WasteMonth startWasteMonth, endWasteMonth;
        if (startMonthGiven && endMonthGiven) {
            startWasteMonth = ParserUtil.parseWasteMonth(startWmString.get());
            endWasteMonth = ParserUtil.parseWasteMonth(endWmString.get());
        } else if (startMonthGiven && !endMonthGiven) {
            startWasteMonth = ParserUtil.parseWasteMonth(startWmString.get());
            endWasteMonth = startWasteMonth.addWasteMonth(ONE_PERIOD);
        } else if (!startMonthGiven && endMonthGiven) {
            endWasteMonth = ParserUtil.parseWasteMonth(endWmString.get());
            startWasteMonth = endWasteMonth.minusWasteMonth(ONE_PERIOD);
        } else {
            endWasteMonth = WasteMonth.getCurrentWasteMonth();
            startWasteMonth = endWasteMonth.minusWasteMonth(ONE_PERIOD);
        }

        if (startWasteMonth.isAfter(endWasteMonth)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ReportWasteCommand.MESSAGE_USAGE));
        }

        return new ReportWasteCommand(startWasteMonth, endWasteMonth);
    }
}
