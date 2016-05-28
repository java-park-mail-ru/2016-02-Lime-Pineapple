package db.models.validation;


public interface IValidate {
    // method throws exception if something isn't right
    void Validate() throws ValidationException;
}
