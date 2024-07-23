package com.films4you.main;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * An interface for use on the single class that wraps the call for a requirement, enforcing that
 * they have methods getValueAsString and getHumanReadable.

 * 
 *
 */
public interface RequirementInterface {

  /**
   * Method to get any non-formatted result of implemented requirements. For example:
   * "Action,Comedy,Horror"

   * @return a string which contains the result as text to be processed further.
   */
  public @Nullable String getValueAsString();

  /**
   * Method to format results in a human-readable format. For example: "The top three genres are,
   * starting with most popular: Action, Comedy, Horror"

   * @return a string which contains the result as a human-readable string.
   */
  public @NonNull String getHumanReadable();
}
