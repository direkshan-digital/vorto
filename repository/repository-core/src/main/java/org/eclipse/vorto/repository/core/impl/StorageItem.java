/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.core.impl;

import java.util.Date;
import org.eclipse.vorto.repository.importer.FileUpload;

public class StorageItem {

  private long timeToLive = -1;

  private String key = null;

  private FileUpload value = null;

  private Date creationDate = null;

  public StorageItem(String key, FileUpload value, Date creationDate, long timeToLive) {
    this.key = key;
    this.value = value;
    this.creationDate = creationDate;
    this.timeToLive = timeToLive;
  }

  public String getKey() {
    return key;
  }


  public Date getExpiryDate() {
    long expiry = getCreationDate().getTime() + getTimeToLive() * 1000L;
    return new Date(expiry);
  }


  public FileUpload getValue() {
    return value;
  }


  public long getTimeToLive() {
    return timeToLive;
  }


  public Date getCreationDate() {
    return creationDate;
  }

  public boolean isExpired() {
    return new Date().after(getExpiryDate()) ? true : false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    result = prime * result + (int) (timeToLive ^ (timeToLive >>> 32));
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    StorageItem other = (StorageItem) obj;
    if (creationDate == null) {
      if (other.creationDate != null)
        return false;
    } else if (!creationDate.equals(other.creationDate))
      return false;
    if (key == null) {
      if (other.key != null)
        return false;
    } else if (!key.equals(other.key))
      return false;
    if (timeToLive != other.timeToLive)
      return false;
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "StorageItem [timeToLive=" + timeToLive + ", key=" + key + ", value=" + value
        + ", creationDate=" + creationDate + "]";
  }



}
