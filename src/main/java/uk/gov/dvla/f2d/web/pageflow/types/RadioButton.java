package uk.gov.dvla.f2d.web.pageflow.types;

public final class RadioButton implements ISingleAnswer
{
    private String answer;

    public RadioButton() {
        super();
    }

    @Override
    public String getAnswer() {
        return this.answer;
    }

    @Override
    public void setAnswer(final String newAnswer) {
        this.answer = newAnswer;
    }
}
