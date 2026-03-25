import { Modal } from "@/components/ui/Modal";
import { Input } from "@/components/ui/Input";
import { Button } from "@/components/ui/Button";
import { packageService } from "@/services/packageService";
import type { Package } from "@/types/package";
import { useState, useEffect } from "react";

interface Props {
  isOpen: boolean;
  onClose: () => void;
  packageItem: Package | null;
  onUpdate: (updatedPkg: Package) => void;
}

const EditPackageModal = ({
  isOpen,
  onClose,
  packageItem,
  onUpdate,
}: Props) => {
  const [price, setPrice] = useState("");
  const [durationInDays, setDurationInDays] = useState("");
  const [name, setName] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    if (packageItem) {
      setName(packageItem.name);
      setPrice(packageItem.price.toString());
      setDurationInDays(packageItem.durationInDays.toString());
    } else {
      setName("");
      setPrice("");
      setDurationInDays("");
    }
  }, [packageItem]);

  const handleEditPackage = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!packageItem) return;

    try {
      setIsSubmitting(true);
      const updatedPackage = await packageService.updatePackage(
        packageItem.id,
        {
          name,
          price: Number(price),
          durationDays: Number(durationInDays),
        },
      );
      onUpdate(updatedPackage);
      onClose();
    } catch (error) {
      console.error(error);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Edit Package">
      <form onSubmit={handleEditPackage} className="space-y-4">
        <Input
          label="Plan Name"
          placeholder="e.g. 1 Year Premium"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
        <Input
          label="Price ($)"
          type="number"
          placeholder="e.g. 200"
          value={price}
          onChange={(e) => setPrice(e.target.value)}
          required
        />
        <Input
          label="Duration (days)"
          type="number"
          placeholder="e.g. 30"
          value={durationInDays}
          onChange={(e) => setDurationInDays(e.target.value)}
          required
        />

        <div className="flex justify-end pt-4 mt-6 border-t border-gray-100">
          <Button
            type="button"
            variant="outline"
            className="mr-3"
            onClick={onClose}
            disabled={isSubmitting}
          >
            Cancel
          </Button>
          <Button type="submit" disabled={isSubmitting}>
            {isSubmitting ? "Saving..." : "Edit Package"}
          </Button>
        </div>
      </form>
    </Modal>
  );
};

export default EditPackageModal;
