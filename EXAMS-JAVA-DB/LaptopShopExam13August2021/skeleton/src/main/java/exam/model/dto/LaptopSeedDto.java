package exam.model.dto;
import exam.model.entity.Shop;
import exam.model.entity.WarrantyType;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;


public class LaptopSeedDto {
    private String macAddress;
    private double cpuSpeed;
    private int ram;
    private int storage;
    private String description;
    private BigDecimal price;
    private WarrantyType warrantyType;
    private ShopNameDto shop;

    public LaptopSeedDto() {
    }

    @NotBlank
    @Size(min = 8)
    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @NotNull
    @Positive
    public double getCpuSpeed() {
        return cpuSpeed;
    }

    public void setCpuSpeed(double cpuSpeed) {
        this.cpuSpeed = cpuSpeed;
    }

    @NotNull
    @Min(8)
    @Max(128)
    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    @NotNull
    @Min(128)
    @Max(1024)
    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }


    @NotBlank
    @Size(min = 10)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    @Positive
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    public WarrantyType getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(WarrantyType warrantyType) {
        this.warrantyType = warrantyType;
    }

    @NotNull
    public ShopNameDto getShop() {
        return shop;
    }

    public void setShop(ShopNameDto shop) {
        this.shop = shop;
    }
}
