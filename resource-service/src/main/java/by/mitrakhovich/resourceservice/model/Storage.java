package by.mitrakhovich.resourceservice.model;


import by.mitrakhovich.resourceservice.dal.entity.StorageType;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class Storage {
    Long id;
    StorageType storageType;
    String bucket;
    String path;


    public Storage() {
    }

    public Storage(Long id, StorageType storageType, String bucket, String path) {
        this.id = id;
        this.storageType = storageType;
        this.bucket = bucket;
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public void setStorageType(StorageType storageType) {
        this.storageType = storageType;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
