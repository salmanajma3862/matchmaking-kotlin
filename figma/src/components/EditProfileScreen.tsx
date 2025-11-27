import { useState } from 'react';
import { ImageWithFallback } from './figma/ImageWithFallback';
import {
  ArrowLeft,
  Camera,
  ChevronRight,
  Plus,
  X,
} from 'lucide-react';

interface EditProfileScreenProps {
  profile: {
    name: string;
    age: number;
    image: string;
    bio: string;
    city?: string;
    country?: string;
    profession?: string;
    education?: string;
    height?: number;
    bodyType?: string;
    religion?: string;
    maritalStatus?: string;
    photos: Array<{ url: string; isPrimary: boolean }>;
    interests?: string[];
    hobbies?: string[];
    smoking?: boolean;
    drinking?: boolean;
    dietPreference?: string;
    intention?: string;
  };
  onBack: () => void;
  onSave: (updatedProfile: any) => void;
}

interface FieldItemProps {
  label: string;
  value: string;
  onClick: () => void;
  placeholder?: string;
}

function FieldItem({ label, value, onClick, placeholder }: FieldItemProps) {
  return (
    <button
      onClick={onClick}
      className="w-full flex items-center justify-between p-4 hover:bg-gray-50 transition-colors border-b border-gray-100"
    >
      <div className="text-left flex-1">
        <div className="text-xs text-gray-500 mb-1">{label}</div>
        <div className={value ? 'text-gray-900' : 'text-gray-400'}>
          {value || placeholder || 'Add...'}
        </div>
      </div>
      <ChevronRight className="w-5 h-5 text-gray-400" />
    </button>
  );
}

interface TagInputProps {
  label: string;
  tags: string[];
  onAdd: (tag: string) => void;
  onRemove: (index: number) => void;
  placeholder?: string;
}

function TagInput({ label, tags, onAdd, onRemove, placeholder }: TagInputProps) {
  const [inputValue, setInputValue] = useState('');

  const handleAdd = () => {
    if (inputValue.trim()) {
      onAdd(inputValue.trim());
      setInputValue('');
    }
  };

  return (
    <div className="p-4 border-b border-gray-100">
      <div className="text-xs text-gray-500 mb-2">{label}</div>
      <div className="flex flex-wrap gap-2 mb-3">
        {tags.map((tag, index) => (
          <div
            key={index}
            className="flex items-center gap-1 px-3 py-1.5 bg-pink-50 text-pink-600 rounded-full text-sm"
          >
            <span>{tag}</span>
            <button
              onClick={() => onRemove(index)}
              className="hover:bg-pink-100 rounded-full p-0.5"
            >
              <X className="w-3 h-3" />
            </button>
          </div>
        ))}
      </div>
      <div className="flex gap-2">
        <input
          type="text"
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          onKeyPress={(e) => e.key === 'Enter' && handleAdd()}
          placeholder={placeholder}
          className="flex-1 px-3 py-2 border border-gray-200 rounded-lg focus:outline-none focus:border-pink-500"
        />
        <button
          onClick={handleAdd}
          className="px-4 py-2 bg-pink-500 text-white rounded-lg hover:bg-pink-600 transition-colors"
        >
          <Plus className="w-5 h-5" />
        </button>
      </div>
    </div>
  );
}

export function EditProfileScreen({ profile, onBack, onSave }: EditProfileScreenProps) {
  const [editedProfile, setEditedProfile] = useState(profile);
  const [activePhotos, setActivePhotos] = useState(profile.photos);

  const handleSave = () => {
    onSave({ ...editedProfile, photos: activePhotos });
  };

  const updateField = (field: string, value: any) => {
    setEditedProfile((prev) => ({ ...prev, [field]: value }));
  };

  const addInterest = (interest: string) => {
    setEditedProfile((prev) => ({
      ...prev,
      interests: [...(prev.interests || []), interest],
    }));
  };

  const removeInterest = (index: number) => {
    setEditedProfile((prev) => ({
      ...prev,
      interests: prev.interests?.filter((_, i) => i !== index) || [],
    }));
  };

  const addHobby = (hobby: string) => {
    setEditedProfile((prev) => ({
      ...prev,
      hobbies: [...(prev.hobbies || []), hobby],
    }));
  };

  const removeHobby = (index: number) => {
    setEditedProfile((prev) => ({
      ...prev,
      hobbies: prev.hobbies?.filter((_, i) => i !== index) || [],
    }));
  };

  return (
    <div className="h-screen bg-gray-50 flex flex-col">
      {/* Header */}
      <div className="bg-white px-6 py-4 border-b border-gray-200 flex items-center justify-between">
        <div className="flex items-center gap-4">
          <button
            onClick={onBack}
            className="p-2 hover:bg-gray-100 rounded-full transition-colors"
          >
            <ArrowLeft className="w-5 h-5" />
          </button>
          <span>Edit Profile</span>
        </div>
        <button
          onClick={handleSave}
          className="px-4 py-2 bg-pink-500 text-white rounded-lg hover:bg-pink-600 transition-colors"
        >
          Save
        </button>
      </div>

      {/* Content */}
      <div className="flex-1 overflow-y-auto pb-6">
        {/* Photos Section */}
        <div className="bg-white mt-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">PHOTOS</span>
          </div>
          <div className="p-4">
            <div className="grid grid-cols-3 gap-3">
              {activePhotos.map((photo, index) => (
                <div key={index} className="relative aspect-square">
                  <ImageWithFallback
                    src={photo.url}
                    alt={`Photo ${index + 1}`}
                    className="w-full h-full object-cover rounded-lg"
                  />
                  {photo.isPrimary && (
                    <div className="absolute top-2 left-2 bg-pink-500 text-white text-xs px-2 py-1 rounded">
                      Primary
                    </div>
                  )}
                  <button
                    onClick={() => setActivePhotos(activePhotos.filter((_, i) => i !== index))}
                    className="absolute top-2 right-2 bg-black/50 text-white p-1 rounded-full hover:bg-black/70"
                  >
                    <X className="w-4 h-4" />
                  </button>
                </div>
              ))}
              {activePhotos.length < 6 && (
                <button className="aspect-square border-2 border-dashed border-gray-300 rounded-lg flex items-center justify-center hover:border-pink-500 hover:bg-pink-50 transition-colors">
                  <div className="text-center">
                    <Camera className="w-8 h-8 text-gray-400 mx-auto mb-1" />
                    <span className="text-xs text-gray-500">Add Photo</span>
                  </div>
                </button>
              )}
            </div>
          </div>
        </div>

        {/* Basic Information */}
        <div className="bg-white mt-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">BASIC INFORMATION</span>
          </div>
          <FieldItem
            label="Name"
            value={editedProfile.name}
            onClick={() => {}}
          />
          <div className="p-4 border-b border-gray-100">
            <div className="text-xs text-gray-500 mb-2">Bio</div>
            <textarea
              value={editedProfile.bio}
              onChange={(e) => updateField('bio', e.target.value)}
              placeholder="Tell others about yourself..."
              className="w-full px-3 py-2 border border-gray-200 rounded-lg focus:outline-none focus:border-pink-500 resize-none"
              rows={4}
            />
          </div>
          <FieldItem
            label="Location"
            value={editedProfile.city && editedProfile.country ? `${editedProfile.city}, ${editedProfile.country}` : editedProfile.city || ''}
            onClick={() => {}}
            placeholder="Add your location"
          />
        </div>

        {/* Professional Details */}
        <div className="bg-white mt-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">PROFESSIONAL DETAILS</span>
          </div>
          <FieldItem
            label="Profession"
            value={editedProfile.profession || ''}
            onClick={() => {}}
            placeholder="Add your profession"
          />
          <FieldItem
            label="Education"
            value={editedProfile.education || ''}
            onClick={() => {}}
            placeholder="Add your education"
          />
        </div>

        {/* Personal Details */}
        <div className="bg-white mt-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">PERSONAL DETAILS</span>
          </div>
          <FieldItem
            label="Height"
            value={editedProfile.height ? `${Math.floor(editedProfile.height / 30.48)}'${Math.round((editedProfile.height % 30.48) / 2.54)}"` : ''}
            onClick={() => {}}
            placeholder="Add your height"
          />
          <FieldItem
            label="Body Type"
            value={editedProfile.bodyType || ''}
            onClick={() => {}}
            placeholder="Add body type"
          />
          <FieldItem
            label="Religion"
            value={editedProfile.religion || ''}
            onClick={() => {}}
            placeholder="Add religion"
          />
          <FieldItem
            label="Marital Status"
            value={editedProfile.maritalStatus || ''}
            onClick={() => {}}
            placeholder="Add marital status"
          />
        </div>

        {/* Lifestyle */}
        <div className="bg-white mt-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">LIFESTYLE</span>
          </div>
          <div className="p-4 border-b border-gray-100">
            <div className="flex items-center justify-between">
              <div>
                <div className="text-gray-900">Smoking</div>
                <div className="text-xs text-gray-500">Do you smoke?</div>
              </div>
              <button
                onClick={() => updateField('smoking', !editedProfile.smoking)}
                className={`relative w-12 h-6 rounded-full transition-colors ${
                  editedProfile.smoking ? 'bg-pink-500' : 'bg-gray-300'
                }`}
              >
                <div
                  className={`absolute top-0.5 left-0.5 w-5 h-5 bg-white rounded-full transition-transform ${
                    editedProfile.smoking ? 'translate-x-6' : 'translate-x-0'
                  }`}
                />
              </button>
            </div>
          </div>
          <div className="p-4 border-b border-gray-100">
            <div className="flex items-center justify-between">
              <div>
                <div className="text-gray-900">Drinking</div>
                <div className="text-xs text-gray-500">Do you drink alcohol?</div>
              </div>
              <button
                onClick={() => updateField('drinking', !editedProfile.drinking)}
                className={`relative w-12 h-6 rounded-full transition-colors ${
                  editedProfile.drinking ? 'bg-pink-500' : 'bg-gray-300'
                }`}
              >
                <div
                  className={`absolute top-0.5 left-0.5 w-5 h-5 bg-white rounded-full transition-transform ${
                    editedProfile.drinking ? 'translate-x-6' : 'translate-x-0'
                  }`}
                />
              </button>
            </div>
          </div>
          <FieldItem
            label="Diet Preference"
            value={editedProfile.dietPreference || ''}
            onClick={() => {}}
            placeholder="Add diet preference"
          />
        </div>

        {/* Interests & Hobbies */}
        <div className="bg-white mt-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">INTERESTS & HOBBIES</span>
          </div>
          <TagInput
            label="Interests"
            tags={editedProfile.interests || []}
            onAdd={addInterest}
            onRemove={removeInterest}
            placeholder="Add an interest..."
          />
          <TagInput
            label="Hobbies"
            tags={editedProfile.hobbies || []}
            onAdd={addHobby}
            onRemove={removeHobby}
            placeholder="Add a hobby..."
          />
        </div>

        {/* Looking For */}
        <div className="bg-white mt-4 mb-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">LOOKING FOR</span>
          </div>
          <FieldItem
            label="Intention"
            value={editedProfile.intention ? editedProfile.intention.replace('_', ' ').charAt(0).toUpperCase() + editedProfile.intention.replace('_', ' ').slice(1) : ''}
            onClick={() => {}}
            placeholder="What are you looking for?"
          />
        </div>
      </div>
    </div>
  );
}
