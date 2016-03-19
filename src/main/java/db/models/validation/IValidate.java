package db.models.validation;


public interface IValidate {

    void validate() throws ValidationException;
}
