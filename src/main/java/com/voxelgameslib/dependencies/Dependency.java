package com.voxelgameslib.dependencies;

import java.util.Date;
import java.util.Objects;

public class Dependency {

    private String groupId;
    private String artifactId;
    private String verion;
    private Date compileTime;

    public Dependency(String groupId, String artifactId, String verion, Date compileTime) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.verion = verion;
        this.compileTime = compileTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVerion() {
        return verion;
    }

    public void setVerion(String verion) {
        this.verion = verion;
    }

    public Date getCompileTime() {
        return compileTime;
    }

    public void setCompileTime(Date compileTime) {
        this.compileTime = compileTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency that = (Dependency) o;
        return Objects.equals(groupId, that.groupId) &&
            Objects.equals(artifactId, that.artifactId) &&
            Objects.equals(verion, that.verion) &&
            Objects.equals(compileTime, that.compileTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(groupId, artifactId, verion, compileTime);
    }

    @Override
    public String toString() {
        return "Dependency{" +
            "groupId='" + groupId + '\'' +
            ", artifactId='" + artifactId + '\'' +
            ", verion='" + verion + '\'' +
            ", compileTime=" + compileTime +
            '}';
    }
}
