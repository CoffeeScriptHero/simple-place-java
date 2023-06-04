package com.kozarenko.exception.custom;

import static com.kozarenko.util.Constants.Exception.COMMENT_NOT_FOUND;

public class NoCommentWithSuchIdException extends Exception {
  public NoCommentWithSuchIdException() {
    super(COMMENT_NOT_FOUND);
  }
}
