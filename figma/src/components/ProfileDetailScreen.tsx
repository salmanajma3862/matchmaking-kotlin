import { useState } from 'react';
import { ImageWithFallback } from './figma/ImageWithFallback';
import {
  ArrowLeft,
  MapPin,
  Briefcase,
  GraduationCap,
  Heart,
  MessageCircle,
  MoreVertical,
  CheckCircle,
  Calendar,
  Users,
  Home,
  Wine,
  Cigarette,
  X,
  Utensils,
  Target,
  Clock,
  BookOpen,
} from 'lucide-react';

interface UserProfile {
  id: string;
  name: string;
  age: number;
  gender: string;
  bio: string;
  height?: number;
  weight?: number;
  bodyType?: string;
  city?: string;
  country?: string;
  religion?: string;
  sect?: string;
  maritalStatus?: string;
  education?: string;
  profession?: string;
  incomeRange?: string;
  photos: Array<{ url: string; isPrimary: boolean }>;
  interests?: string[];
  hobbies?: string[];
  smoking?: boolean;
  drinking?: boolean;
  dietPreference?: string;
  familyBackground?: string;
  numberOfSiblings?: number;
  livingWithFamily?: boolean;
  intention?: string;
  readyForMarriageTimeframe?: string;
  isVerified?: boolean;
}

interface ProfileDetailScreenProps {
  profile: UserProfile;
  onBack: () => void;
  onMessage: () => void;
  onLike?: () => void;
}

export function ProfileDetailScreen({ profile, onBack, onMessage, onLike }: ProfileDetailScreenProps) {
  const [currentPhotoIndex, setCurrentPhotoIndex] = useState(0);

  const calculateAge = (age: number) => age;

  const formatHeight = (height?: number) => {
    if (!height) return null;
    const feet = Math.floor(height / 30.48);
    const inches = Math.round((height % 30.48) / 2.54);
    return `${feet}'${inches}"`;
  };

  return (
    <div className="h-screen bg-gray-50 flex flex-col">
      {/* Photo Gallery */}
      <div className="relative h-[60vh] bg-black">
        <ImageWithFallback
          src={profile.photos[currentPhotoIndex]?.url || profile.photos[0]?.url}
          alt={profile.name}
          className="w-full h-full object-cover"
        />
        
        {/* Gradient Overlay */}
        <div className="absolute inset-0 bg-gradient-to-b from-black/40 via-transparent to-transparent" />

        {/* Header Controls */}
        <div className="absolute top-0 left-0 right-0 p-4 flex items-center justify-between">
          <button
            onClick={onBack}
            className="p-2 bg-white/20 backdrop-blur-sm rounded-full hover:bg-white/30 transition-colors"
          >
            <ArrowLeft className="w-5 h-5 text-white" />
          </button>
          <button className="p-2 bg-white/20 backdrop-blur-sm rounded-full hover:bg-white/30 transition-colors">
            <MoreVertical className="w-5 h-5 text-white" />
          </button>
        </div>

        {/* Photo Indicators */}
        {profile.photos.length > 1 && (
          <div className="absolute top-20 left-0 right-0 px-4">
            <div className="flex gap-1">
              {profile.photos.map((_, index) => (
                <button
                  key={index}
                  onClick={() => setCurrentPhotoIndex(index)}
                  className={`flex-1 h-1 rounded-full transition-all ${
                    index === currentPhotoIndex ? 'bg-white' : 'bg-white/40'
                  }`}
                />
              ))}
            </div>
          </div>
        )}
      </div>

      {/* Content */}
      <div className="flex-1 overflow-y-auto -mt-8">
        <div className="bg-white rounded-t-3xl shadow-lg">
          {/* Basic Info */}
          <div className="p-6 border-b border-gray-100">
            <div className="flex items-start justify-between mb-2">
              <div className="flex-1">
                <div className="flex items-center gap-2 mb-1">
                  <span className="text-2xl">{profile.name}, {profile.age}</span>
                  {profile.isVerified && (
                    <CheckCircle className="w-5 h-5 text-blue-500 fill-blue-500" />
                  )}
                </div>
                {profile.city && profile.country && (
                  <div className="flex items-center gap-1 text-gray-600 mb-3">
                    <MapPin className="w-4 h-4" />
                    <span className="text-sm">{profile.city}, {profile.country}</span>
                  </div>
                )}
              </div>
            </div>

            {/* Quick Stats */}
            <div className="flex gap-4 mb-4">
              {profile.profession && (
                <div className="flex items-center gap-1.5 text-sm text-gray-600">
                  <Briefcase className="w-4 h-4" />
                  <span>{profile.profession}</span>
                </div>
              )}
              {profile.education && (
                <div className="flex items-center gap-1.5 text-sm text-gray-600">
                  <GraduationCap className="w-4 h-4" />
                  <span>{profile.education}</span>
                </div>
              )}
            </div>

            {/* Bio */}
            {profile.bio && (
              <p className="text-gray-700 leading-relaxed">{profile.bio}</p>
            )}
          </div>

          {/* Basic Details Section */}
          <div className="p-6 border-b border-gray-100">
            <span className="text-sm text-gray-500 mb-3 block">Basic Details</span>
            <div className="grid grid-cols-2 gap-4">
              {profile.height && (
                <div>
                  <span className="text-xs text-gray-500 block mb-1">Height</span>
                  <span className="text-sm">{formatHeight(profile.height)}</span>
                </div>
              )}
              {profile.bodyType && (
                <div>
                  <span className="text-xs text-gray-500 block mb-1">Body Type</span>
                  <span className="text-sm">{profile.bodyType}</span>
                </div>
              )}
              {profile.maritalStatus && (
                <div>
                  <span className="text-xs text-gray-500 block mb-1">Marital Status</span>
                  <span className="text-sm">{profile.maritalStatus}</span>
                </div>
              )}
              {profile.religion && (
                <div>
                  <span className="text-xs text-gray-500 block mb-1">Religion</span>
                  <span className="text-sm">{profile.religion}</span>
                </div>
              )}
              {profile.sect && (
                <div>
                  <span className="text-xs text-gray-500 block mb-1">Sect</span>
                  <span className="text-sm">{profile.sect}</span>
                </div>
              )}
              {profile.incomeRange && (
                <div>
                  <span className="text-xs text-gray-500 block mb-1">Income Range</span>
                  <span className="text-sm">{profile.incomeRange}</span>
                </div>
              )}
            </div>
          </div>

          {/* Lifestyle Section */}
          <div className="p-6 border-b border-gray-100">
            <span className="text-sm text-gray-500 mb-3 block">Lifestyle</span>
            <div className="flex flex-wrap gap-2">
              <div className="flex items-center gap-2 px-3 py-2 bg-gray-50 rounded-full">
                <Cigarette className="w-4 h-4" />
                <span className="text-sm">
                  {profile.smoking ? 'Smoker' : 'Non-smoker'}
                </span>
              </div>
              <div className="flex items-center gap-2 px-3 py-2 bg-gray-50 rounded-full">
                <Wine className="w-4 h-4" />
                <span className="text-sm">
                  {profile.drinking ? 'Drinks' : "Doesn't drink"}
                </span>
              </div>
              {profile.dietPreference && (
                <div className="flex items-center gap-2 px-3 py-2 bg-gray-50 rounded-full">
                  <Utensils className="w-4 h-4" />
                  <span className="text-sm">{profile.dietPreference}</span>
                </div>
              )}
            </div>
          </div>

          {/* Looking For Section */}
          {(profile.intention || profile.readyForMarriageTimeframe) && (
            <div className="p-6 border-b border-gray-100">
              <span className="text-sm text-gray-500 mb-3 block">Looking For</span>
              <div className="space-y-3">
                {profile.intention && (
                  <div className="flex items-center gap-2">
                    <Target className="w-4 h-4 text-gray-600" />
                    <span className="text-sm capitalize">{profile.intention.replace('_', ' ')}</span>
                  </div>
                )}
                {profile.readyForMarriageTimeframe && (
                  <div className="flex items-center gap-2">
                    <Clock className="w-4 h-4 text-gray-600" />
                    <span className="text-sm">Ready {profile.readyForMarriageTimeframe.replace('_', ' ')}</span>
                  </div>
                )}
              </div>
            </div>
          )}

          {/* Interests & Hobbies */}
          {(profile.interests && profile.interests.length > 0) || (profile.hobbies && profile.hobbies.length > 0) ? (
            <div className="p-6 border-b border-gray-100">
              <span className="text-sm text-gray-500 mb-3 block">Interests & Hobbies</span>
              <div className="flex flex-wrap gap-2">
                {profile.interests?.map((interest, index) => (
                  <span
                    key={`interest-${index}`}
                    className="px-3 py-1.5 bg-pink-50 text-pink-600 rounded-full text-sm"
                  >
                    {interest}
                  </span>
                ))}
                {profile.hobbies?.map((hobby, index) => (
                  <span
                    key={`hobby-${index}`}
                    className="px-3 py-1.5 bg-purple-50 text-purple-600 rounded-full text-sm"
                  >
                    {hobby}
                  </span>
                ))}
              </div>
            </div>
          ) : null}

          {/* Family Background */}
          {(profile.familyBackground || profile.numberOfSiblings !== undefined || profile.livingWithFamily !== undefined) && (
            <div className="p-6 border-b border-gray-100">
              <span className="text-sm text-gray-500 mb-3 block">Family Background</span>
              <div className="space-y-3">
                {profile.familyBackground && (
                  <div className="flex items-start gap-2">
                    <BookOpen className="w-4 h-4 text-gray-600 mt-0.5" />
                    <span className="text-sm flex-1">{profile.familyBackground}</span>
                  </div>
                )}
                {profile.numberOfSiblings !== undefined && (
                  <div className="flex items-center gap-2">
                    <Users className="w-4 h-4 text-gray-600" />
                    <span className="text-sm">{profile.numberOfSiblings} sibling{profile.numberOfSiblings !== 1 ? 's' : ''}</span>
                  </div>
                )}
                {profile.livingWithFamily !== undefined && (
                  <div className="flex items-center gap-2">
                    <Home className="w-4 h-4 text-gray-600" />
                    <span className="text-sm">
                      {profile.livingWithFamily ? 'Living with family' : 'Living independently'}
                    </span>
                  </div>
                )}
              </div>
            </div>
          )}

          {/* Bottom Spacing */}
          <div className="h-24" />
        </div>
      </div>

      {/* Bottom Action Buttons */}
      <div className="absolute bottom-0 left-0 right-0 p-4 bg-white border-t border-gray-200">
        <div className="flex gap-3 max-w-3xl mx-auto">
          {onLike && (
            <button
              onClick={onLike}
              className="p-4 bg-gray-100 rounded-full hover:bg-gray-200 transition-colors"
            >
              <Heart className="w-6 h-6 text-pink-500" />
            </button>
          )}
          <button
            onClick={onMessage}
            className="flex-1 py-3 bg-gradient-to-r from-pink-500 to-rose-500 text-white rounded-full hover:shadow-lg transition-all flex items-center justify-center gap-2"
          >
            <MessageCircle className="w-5 h-5" />
            <span>Message</span>
          </button>
        </div>
      </div>
    </div>
  );
}
