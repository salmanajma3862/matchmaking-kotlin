import { useState } from 'react';
import { Heart, MessageCircle, User } from 'lucide-react';
import { MatchCard } from './components/MatchCard';
import { ConversationItem } from './components/ConversationItem';
import { MessageThread } from './components/MessageThread';
import { MatchScreen } from './components/MatchScreen';
import { ProfileDetailScreen } from './components/ProfileDetailScreen';
import { ProfileScreen } from './components/ProfileScreen';
import { SettingsScreen } from './components/SettingsScreen';
import { NotificationsScreen } from './components/NotificationsScreen';
import { EditProfileScreen } from './components/EditProfileScreen';

interface Match {
  id: string;
  name: string;
  age: number;
  image: string;
  location: string;
  isNew?: boolean;
  // Profile details
  gender?: string;
  bio?: string;
  height?: number;
  bodyType?: string;
  city?: string;
  country?: string;
  religion?: string;
  sect?: string;
  maritalStatus?: string;
  education?: string;
  profession?: string;
  incomeRange?: string;
  photos?: Array<{ url: string; isPrimary: boolean }>;
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

interface Conversation {
  id: string;
  name: string;
  image: string;
  lastMessage: string;
  timestamp: string;
  unread?: boolean;
  messages: Array<{
    id: string;
    text: string;
    timestamp: string;
    isSent: boolean;
    isEdited?: boolean;
    replyTo?: {
      text: string;
      sender: string;
    };
  }>;
}

export default function App() {
  const [activeConversation, setActiveConversation] = useState<string | null>(null);
  const [showMatchScreen, setShowMatchScreen] = useState(false);
  const [showProfileDetailScreen, setShowProfileDetailScreen] = useState(false);
  const [selectedMatch, setSelectedMatch] = useState<Match | null>(null);
  const [showProfileScreen, setShowProfileScreen] = useState(false);
  const [showSettingsScreen, setShowSettingsScreen] = useState(false);
  const [showNotificationsScreen, setShowNotificationsScreen] = useState(false);
  const [viewingOwnProfile, setViewingOwnProfile] = useState(false);
  const [showEditProfileScreen, setShowEditProfileScreen] = useState(false);
  
  const matches: Match[] = [
    {
      id: '1',
      name: 'Emma',
      age: 28,
      image: 'https://images.unsplash.com/photo-1594318223885-20dc4b889f9e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMHBvcnRyYWl0JTIwc21pbGluZ3xlbnwxfHx8fDE3NjQwOTcyMTR8MA&ixlib=rb-4.1.0&q=80&w=1080',
      location: 'New York, NY',
      isNew: true,
      gender: 'Female',
      bio: 'Loves hiking and exploring new places. Enjoys cooking and trying out new recipes.',
      height: 5.5,
      bodyType: 'Slim',
      city: 'New York',
      country: 'USA',
      religion: 'Christianity',
      sect: 'Catholic',
      maritalStatus: 'Single',
      education: 'Bachelor\'s in Marketing',
      profession: 'Marketing Manager',
      incomeRange: '$50,000 - $70,000',
      photos: [
        { url: 'https://images.unsplash.com/photo-1594318223885-20dc4b889f9e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMHBvcnRyYWl0JTIwc21pbGluZ3xlbnwxfHx8fDE3NjQwOTcyMTR8MA&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: true },
        { url: 'https://images.unsplash.com/photo-1644313720910-9a2520bbd28f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMG91dGRvb3IlMjBwb3J0cmFpdHxlbnwxfHx8fDE3NjQwNzQ3NTN8MA&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: false },
      ],
      interests: ['Hiking', 'Cooking', 'Traveling'],
      hobbies: ['Reading', 'Yoga'],
      smoking: false,
      drinking: false,
      dietPreference: 'Vegetarian',
      familyBackground: 'Middle-class family',
      numberOfSiblings: 2,
      livingWithFamily: true,
      intention: 'Long-term relationship',
      readyForMarriageTimeframe: 'Within 2 years',
      isVerified: true,
    },
    {
      id: '2',
      name: 'James',
      age: 34,
      image: 'https://images.unsplash.com/photo-1695485121912-25c7ea05119c?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxtYW4lMjBwb3J0cmFpdCUyMGNhc3VhbHxlbnwxfHx8fDE3NjQxMjk3MDN8MA&ixlib=rb-4.1.0&q=80&w=1080',
      location: 'Los Angeles, CA',
      isNew: true,
      gender: 'Male',
      bio: 'Travel enthusiast with a passion for photography. Loves outdoor activities and adventure.',
      height: 6.0,
      bodyType: 'Athletic',
      city: 'Los Angeles',
      country: 'USA',
      religion: 'Christianity',
      sect: 'Protestant',
      maritalStatus: 'Single',
      education: 'Bachelor\'s in Photography',
      profession: 'Freelance Photographer',
      incomeRange: '$60,000 - $80,000',
      photos: [
        { url: 'https://images.unsplash.com/photo-1695485121912-25c7ea05119c?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxtYW4lMjBwb3J0cmFpdCUyMGNhc3VhbHxlbnwxfHx8fDE3NjQxMjk3MDN8MA&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: true },
        { url: 'https://images.unsplash.com/photo-1644313720910-9a2520bbd28f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMG91dGRvb3IlMjBwb3J0cmFpdHxlbnwxfHx8fDE3NjQwNzQ3NTN8MA&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: false },
      ],
      interests: ['Traveling', 'Photography', 'Outdoor activities'],
      hobbies: ['Hiking', 'Camping'],
      smoking: false,
      drinking: false,
      dietPreference: 'Non-vegetarian',
      familyBackground: 'Upper-middle-class family',
      numberOfSiblings: 1,
      livingWithFamily: false,
      intention: 'Long-term relationship',
      readyForMarriageTimeframe: 'Within 3 years',
      isVerified: true,
    },
    {
      id: '3',
      name: 'Sophia',
      age: 25,
      image: 'https://images.unsplash.com/photo-1644313720910-9a2520bbd28f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMG91dGRvb3IlMjBwb3J0cmFpdHxlbnwxfHx8fDE3NjQwNzQ3NTN8MA&ixlib=rb-4.1.0&q=80&w=1080',
      location: 'Chicago, IL',
      gender: 'Female',
      bio: 'Dog lover with a passion for art. Enjoys painting and spending time with her dog Max.',
      height: 5.3,
      bodyType: 'Slim',
      city: 'Chicago',
      country: 'USA',
      religion: 'Christianity',
      sect: 'Catholic',
      maritalStatus: 'Single',
      education: 'Bachelor\'s in Fine Arts',
      profession: 'Artist',
      incomeRange: '$40,000 - $60,000',
      photos: [
        { url: 'https://images.unsplash.com/photo-1644313720910-9a2520bbd28f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMG91dGRvb3IlMjBwb3J0cmFpdHxlbnwxfHx8fDE3NjQwNzQ3NTN8MA&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: true },
        { url: 'https://images.unsplash.com/photo-1644313720910-9a2520bbd28f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMG91dGRvb3IlMjBwb3J0cmFpdHxlbnwxfHx8fDE3NjQwNzQ3NTN8MA&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: false },
      ],
      interests: ['Art', 'Dog walking', 'Painting'],
      hobbies: ['Reading', 'Yoga'],
      smoking: false,
      drinking: false,
      dietPreference: 'Vegetarian',
      familyBackground: 'Middle-class family',
      numberOfSiblings: 2,
      livingWithFamily: true,
      intention: 'Long-term relationship',
      readyForMarriageTimeframe: 'Within 2 years',
      isVerified: true,
    },
    {
      id: '4',
      name: 'Alex',
      age: 30,
      image: 'https://images.unsplash.com/photo-1622812947502-0a643f17387e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxtYW4lMjBzbWlsaW5nJTIwcG9ydHJhaXR8ZW58MXx8fHwxNzY0MTIyMTAyfDA&ixlib=rb-4.1.0&q=80&w=1080',
      location: 'San Francisco, CA',
      gender: 'Male',
      bio: 'Tech enthusiast with a passion for coding. Loves outdoor activities and adventure.',
      height: 5.9,
      bodyType: 'Athletic',
      city: 'San Francisco',
      country: 'USA',
      religion: 'Christianity',
      sect: 'Protestant',
      maritalStatus: 'Single',
      education: 'Bachelor\'s in Computer Science',
      profession: 'Software Engineer',
      incomeRange: '$80,000 - $100,000',
      photos: [
        { url: 'https://images.unsplash.com/photo-1622812947502-0a643f17387e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxtYW4lMjBzbWlsaW5nJTIwcG9ydHJhaXR8ZW58MXx8fHwxNzY0MTIyMTAyfDA&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: true },
        { url: 'https://images.unsplash.com/photo-1644313720910-9a2520bbd28f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMG91dGRvb3IlMjBwb3J0cmFpdHxlbnwxfHx8fDE3NjQwNzQ3NTN8MA&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: false },
      ],
      interests: ['Coding', 'Outdoor activities', 'Adventure'],
      hobbies: ['Hiking', 'Camping'],
      smoking: false,
      drinking: false,
      dietPreference: 'Non-vegetarian',
      familyBackground: 'Upper-middle-class family',
      numberOfSiblings: 1,
      livingWithFamily: false,
      intention: 'Long-term relationship',
      readyForMarriageTimeframe: 'Within 3 years',
      isVerified: true,
    },
    {
      id: '5',
      name: 'Olivia',
      age: 27,
      image: 'https://images.unsplash.com/photo-1607748725046-cd194b716604?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMGhhcHB5JTIwcG9ydHJhaXR8ZW58MXx8fHwxNzY0MTIyMTAyfDA&ixlib=rb-4.1.0&q=80&w=1080',
      location: 'Boston, MA',
      gender: 'Female',
      bio: 'Dog lover with a passion for art. Enjoys painting and spending time with her dog Max.',
      height: 5.3,
      bodyType: 'Slim',
      city: 'Boston',
      country: 'USA',
      religion: 'Christianity',
      sect: 'Catholic',
      maritalStatus: 'Single',
      education: 'Bachelor\'s in Fine Arts',
      profession: 'Artist',
      incomeRange: '$40,000 - $60,000',
      photos: [
        { url: 'https://images.unsplash.com/photo-1607748725046-cd194b716604?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMGhhcHB5JTIwcG9ydHJhaXR8ZW58MXx8fHwxNzY0MTIyMTAyfDA&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: true },
        { url: 'https://images.unsplash.com/photo-1644313720910-9a2520bbd28f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMG91dGRvb3IlMjBwb3J0cmFpdHxlbnwxfHx8fDE3NjQwNzQ3NTN8MA&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: false },
      ],
      interests: ['Art', 'Dog walking', 'Painting'],
      hobbies: ['Reading', 'Yoga'],
      smoking: false,
      drinking: false,
      dietPreference: 'Vegetarian',
      familyBackground: 'Middle-class family',
      numberOfSiblings: 2,
      livingWithFamily: true,
      intention: 'Long-term relationship',
      readyForMarriageTimeframe: 'Within 2 years',
      isVerified: true,
    },
    {
      id: '6',
      name: 'Isabella',
      age: 26,
      image: 'https://images.unsplash.com/photo-1649589244330-09ca58e4fa64?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMHBvcnRyYWl0JTIwcHJvZmVzc2lvbmFsfGVufDF8fHx8MTc2NDA5MDA5NXww&ixlib=rb-4.1.0&q=80&w=1080',
      location: 'Miami, FL',
      gender: 'Female',
      bio: 'Dog lover with a passion for art. Enjoys painting and spending time with her dog Max.',
      height: 5.3,
      bodyType: 'Slim',
      city: 'Miami',
      country: 'USA',
      religion: 'Christianity',
      sect: 'Catholic',
      maritalStatus: 'Single',
      education: 'Bachelor\'s in Fine Arts',
      profession: 'Artist',
      incomeRange: '$40,000 - $60,000',
      photos: [
        { url: 'https://images.unsplash.com/photo-1649589244330-09ca58e4fa64?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMHBvcnRyYWl0JTIwcHJvZmVzc2lvbmFsfGVufDF8fHx8MTc2NDA5MDA5NXww&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: true },
        { url: 'https://images.unsplash.com/photo-1644313720910-9a2520bbd28f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMG91dGRvb3IlMjBwb3J0cmFpdHxlbnwxfHx8fDE3NjQwNzQ3NTN8MA&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: false },
      ],
      interests: ['Art', 'Dog walking', 'Painting'],
      hobbies: ['Reading', 'Yoga'],
      smoking: false,
      drinking: false,
      dietPreference: 'Vegetarian',
      familyBackground: 'Middle-class family',
      numberOfSiblings: 2,
      livingWithFamily: true,
      intention: 'Long-term relationship',
      readyForMarriageTimeframe: 'Within 2 years',
      isVerified: true,
    },
    {
      id: '7',
      name: 'Daniel',
      age: 32,
      image: 'https://images.unsplash.com/photo-1695485121912-25c7ea05119c?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxtYW4lMjBjYXN1YWwlMjBwb3J0cmFpdHxlbnwxfHx8fDE3NjQxMzYxNjl8MA&ixlib=rb-4.1.0&q=80&w=1080',
      location: 'Seattle, WA',
      gender: 'Male',
      bio: 'Travel enthusiast with a passion for photography. Loves outdoor activities and adventure.',
      height: 6.0,
      bodyType: 'Athletic',
      city: 'Seattle',
      country: 'USA',
      religion: 'Christianity',
      sect: 'Protestant',
      maritalStatus: 'Single',
      education: 'Bachelor\'s in Photography',
      profession: 'Freelance Photographer',
      incomeRange: '$60,000 - $80,000',
      photos: [
        { url: 'https://images.unsplash.com/photo-1695485121912-25c7ea05119c?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxtYW4lMjBjYXN1YWwlMjBwb3J0cmFpdHxlbnwxfHx8fDE3NjQxMzYxNjl8MA&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: true },
        { url: 'https://images.unsplash.com/photo-1644313720910-9a2520bbd28f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMG91dGRvb3IlMjBwb3J0cmFpdHxlbnwxfHx8fDE3NjQwNzQ3NTN8MA&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: false },
      ],
      interests: ['Traveling', 'Photography', 'Outdoor activities'],
      hobbies: ['Hiking', 'Camping'],
      smoking: false,
      drinking: false,
      dietPreference: 'Non-vegetarian',
      familyBackground: 'Upper-middle-class family',
      numberOfSiblings: 1,
      livingWithFamily: false,
      intention: 'Long-term relationship',
      readyForMarriageTimeframe: 'Within 3 years',
      isVerified: true,
    },
    {
      id: '8',
      name: 'Charlotte',
      age: 29,
      image: 'https://images.unsplash.com/photo-1745434159123-4908d0b9df94?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMHNtaWxpbmclMjBoZWFkc2hvdHxlbnwxfHx8fDE3NjQwMzE3MDN8MA&ixlib=rb-4.1.0&q=80&w=1080',
      location: 'Austin, TX',
      gender: 'Female',
      bio: 'Dog lover with a passion for art. Enjoys painting and spending time with her dog Max.',
      height: 5.3,
      bodyType: 'Slim',
      city: 'Austin',
      country: 'USA',
      religion: 'Christianity',
      sect: 'Catholic',
      maritalStatus: 'Single',
      education: 'Bachelor\'s in Fine Arts',
      profession: 'Artist',
      incomeRange: '$40,000 - $60,000',
      photos: [
        { url: 'https://images.unsplash.com/photo-1745434159123-4908d0b9df94?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMHNtaWxpbmclMjBoZWFkc2hvdHxlbnwxfHx8fDE3NjQwMzE3MDN8MA&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: true },
        { url: 'https://images.unsplash.com/photo-1644313720910-9a2520bbd28f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMG91dGRvb3IlMjBwb3J0cmFpdHxlbnwxfHx8fDE3NjQwNzQ3NTN8MA&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: false },
      ],
      interests: ['Art', 'Dog walking', 'Painting'],
      hobbies: ['Reading', 'Yoga'],
      smoking: false,
      drinking: false,
      dietPreference: 'Vegetarian',
      familyBackground: 'Middle-class family',
      numberOfSiblings: 2,
      livingWithFamily: true,
      intention: 'Long-term relationship',
      readyForMarriageTimeframe: 'Within 2 years',
      isVerified: true,
    },
  ];

  const [conversations, setConversations] = useState<Conversation[]>([
    {
      id: '1',
      name: 'Emma',
      image: 'https://images.unsplash.com/photo-1594318223885-20dc4b889f9e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMHBvcnRyYWl0JTIwc21pbGluZ3xlbnwxfHx8fDE3NjQwOTcyMTR8MA&ixlib=rb-4.1.0&q=80&w=1080',
      lastMessage: "That sounds great! Let's do it 😊",
      timestamp: '2m',
      unread: true,
      messages: [
        {
          id: 'm1',
          text: "Hey! How's it going?",
          timestamp: '10:30 AM',
          isSent: false,
        },
        {
          id: 'm2',
          text: "Hi! I'm doing great, thanks! How about you?",
          timestamp: '10:32 AM',
          isSent: true,
        },
        {
          id: 'm3',
          text: "Pretty good! I saw you like hiking. Have you been to any good trails recently?",
          timestamp: '10:33 AM',
          isSent: false,
        },
        {
          id: 'm4',
          text: "Yes! I went to Eagle Peak last weekend. The views were incredible!",
          timestamp: '10:35 AM',
          isSent: true,
          isEdited: true,
        },
        {
          id: 'm5',
          text: "That sounds amazing! Would you want to go hiking together sometime?",
          timestamp: '10:36 AM',
          isSent: false,
        },
        {
          id: 'm6',
          text: "That sounds great! Let's do it 😊",
          timestamp: '10:38 AM',
          isSent: true,
          replyTo: {
            text: "That sounds amazing! Would you want to go hiking together sometime?",
            sender: "Emma"
          }
        },
      ],
    },
    {
      id: '2',
      name: 'James',
      image: 'https://images.unsplash.com/photo-1695485121912-25c7ea05119c?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxtYW4lMjBwb3J0cmFpdCUyMGNhc3VhbHxlbnwxfHx8fDE3NjQxMjk3MDN8MA&ixlib=rb-4.1.0&q=80&w=1080',
      lastMessage: 'Looking forward to it!',
      timestamp: '1h',
      messages: [
        {
          id: 'm1',
          text: "Hi there! Love your travel photos!",
          timestamp: '9:15 AM',
          isSent: false,
        },
        {
          id: 'm2',
          text: "Thank you! Travel is my passion. Where's your favorite place you've been?",
          timestamp: '9:20 AM',
          isSent: true,
        },
        {
          id: 'm3',
          text: "Probably Japan. The culture and food were incredible!",
          timestamp: '9:22 AM',
          isSent: false,
          isEdited: true,
        },
        {
          id: 'm4',
          text: "Japan is on my bucket list! We should grab coffee and you can tell me all about it?",
          timestamp: '9:25 AM',
          isSent: true,
        },
        {
          id: 'm5',
          text: 'Looking forward to it!',
          timestamp: '9:28 AM',
          isSent: false,
          replyTo: {
            text: "Japan is on my bucket list! We should grab coffee and you can tell me all about it?",
            sender: "You"
          }
        },
      ],
    },
    {
      id: '3',
      name: 'Sophia',
      image: 'https://images.unsplash.com/photo-1644313720910-9a2520bbd28f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3b21hbiUyMG91dGRvb3IlMjBwb3J0cmFpdHxlbnwxfHx8fDE3NjQwNzQ3NTN8MA&ixlib=rb-4.1.0&q=80&w=1080',
      lastMessage: "I'd love to hear your recommendations!",
      timestamp: '3h',
      messages: [
        {
          id: 'm1',
          text: "Your dog is adorable! What's their name?",
          timestamp: 'Yesterday',
          isSent: false,
        },
        {
          id: 'm2',
          text: "That's Max! He's a golden retriever 🐕",
          timestamp: 'Yesterday',
          isSent: true,
        },
        {
          id: 'm3',
          text: "I love golden retrievers! Do you take him to any dog parks around here?",
          timestamp: 'Yesterday',
          isSent: false,
        },
        {
          id: 'm4',
          text: "I'd love to hear your recommendations!",
          timestamp: 'Yesterday',
          isSent: true,
        },
      ],
    },
    {
      id: '4',
      name: 'Alex',
      image: 'https://images.unsplash.com/photo-1622812947502-0a643f17387e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxtYW4lMjBzbWlsaW5nJTIwcG9ydHJhaXR8ZW58MXx8fHwxNzY0MTIyMTAyfDA&ixlib=rb-4.1.0&q=80&w=1080',
      lastMessage: "Haha that's hilarious! 😂",
      timestamp: '1d',
      messages: [
        {
          id: 'm1',
          text: "So I have to ask... pineapple on pizza?",
          timestamp: '2 days ago',
          isSent: false,
        },
        {
          id: 'm2',
          text: "Absolutely not! That's a crime against pizza 🍕",
          timestamp: '2 days ago',
          isSent: true,
        },
        {
          id: 'm3',
          text: "Haha that's hilarious! 😂",
          timestamp: '2 days ago',
          isSent: false,
        },
      ],
    },
    {
      id: '5',
      name: 'Michael',
      image: 'https://images.unsplash.com/photo-1672685667592-0392f458f46f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxtYW4lMjBwcm9mZXNzaW9uYWwlMjBwb3J0cmFpdHxlbnwxfHx8fDE3NjQwODU2MzF8MA&ixlib=rb-4.1.0&q=80&w=1080',
      lastMessage: 'Nice to meet you!',
      timestamp: '2d',
      messages: [
        {
          id: 'm1',
          text: "Hey! We matched! 🎉",
          timestamp: '3 days ago',
          isSent: false,
        },
        {
          id: 'm2',
          text: 'Nice to meet you!',
          timestamp: '3 days ago',
          isSent: true,
        },
      ],
    },
  ]);

  const activeConvo = conversations.find((c) => c.id === activeConversation);

  const handleSendMessage = (message: string) => {
    if (!activeConversation) return;

    setConversations((prev) =>
      prev.map((conv) =>
        conv.id === activeConversation
          ? {
              ...conv,
              messages: [
                ...conv.messages,
                {
                  id: `m${Date.now()}`,
                  text: message,
                  timestamp: 'Just now',
                  isSent: true,
                },
              ],
              lastMessage: message,
              timestamp: 'Just now',
            }
          : conv
      )
    );
  };

  const handleMatchMessage = (matchId: string) => {
    // Find if conversation exists
    const existingConvo = conversations.find((c) => c.id === matchId);
    if (existingConvo) {
      setShowMatchScreen(false);
      setActiveConversation(matchId);
    } else {
      // Create new conversation
      const match = matches.find((m) => m.id === matchId);
      if (match) {
        const newConvo: Conversation = {
          id: match.id,
          name: match.name,
          image: match.image,
          lastMessage: 'Start a conversation...',
          timestamp: 'Now',
          messages: [],
        };
        setConversations((prev) => [newConvo, ...prev]);
        setShowMatchScreen(false);
        setActiveConversation(matchId);
      }
    }
  };

  const handleViewProfile = (match: Match) => {
    setSelectedMatch(match);
    setShowProfileDetailScreen(true);
  };

  // Current user data (mock data for own profile)
  const currentUser = {
    id: 'current-user',
    name: 'John',
    age: 29,
    image: 'https://images.unsplash.com/photo-1532272278764-53cd1fe53f72?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxwcm9mZXNzaW9uYWwlMjBwb3J0cmFpdCUyMHBob3RvZ3JhcGh5fGVufDF8fHx8MTc2NDI0ODIxMXww&ixlib=rb-4.1.0&q=80&w=1080',
    city: 'New York',
    country: 'USA',
    profileCompletion: 85,
    matchCount: 24,
    viewCount: 156,
    gender: 'Male',
    bio: 'Love traveling, hiking, and good food. Looking for someone to share adventures with.',
    height: 5.11,
    bodyType: 'Athletic',
    religion: 'Christianity',
    maritalStatus: 'Single',
    education: 'Master\'s in Computer Science',
    profession: 'Software Engineer',
    incomeRange: '$80,000 - $100,000',
    photos: [
      { url: 'https://images.unsplash.com/photo-1532272278764-53cd1fe53f72?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxwcm9mZXNzaW9uYWwlMjBwb3J0cmFpdCUyMHBob3RvZ3JhcGh5fGVufDF8fHx8MTc2NDI0ODIxMXww&ixlib=rb-4.1.0&q=80&w=1080', isPrimary: true },
    ],
    interests: ['Travel', 'Hiking', 'Photography', 'Cooking'],
    hobbies: ['Reading', 'Gym', 'Cycling'],
    smoking: false,
    drinking: false,
    dietPreference: 'Non-vegetarian',
    familyBackground: 'Middle-class family',
    numberOfSiblings: 1,
    livingWithFamily: false,
    intention: 'marriage',
    readyForMarriageTimeframe: '1_year',
    isVerified: true,
  };

  // Show edit profile screen
  if (showEditProfileScreen) {
    return (
      <EditProfileScreen
        profile={{
          name: currentUser.name,
          age: currentUser.age,
          image: currentUser.image,
          bio: currentUser.bio,
          city: currentUser.city,
          country: currentUser.country,
          profession: currentUser.profession,
          education: currentUser.education,
          height: currentUser.height,
          bodyType: currentUser.bodyType,
          religion: currentUser.religion,
          maritalStatus: currentUser.maritalStatus,
          photos: currentUser.photos,
          interests: currentUser.interests,
          hobbies: currentUser.hobbies,
          smoking: currentUser.smoking,
          drinking: currentUser.drinking,
          dietPreference: currentUser.dietPreference,
          intention: currentUser.intention,
        }}
        onBack={() => {
          setShowEditProfileScreen(false);
          setShowProfileScreen(true);
        }}
        onSave={(updatedProfile) => {
          console.log('Profile updated:', updatedProfile);
          // Here you would normally update the user profile
          setShowEditProfileScreen(false);
          setShowProfileScreen(true);
        }}
      />
    );
  }

  // Show notifications screen
  if (showNotificationsScreen) {
    return (
      <NotificationsScreen
        onBack={() => {
          setShowNotificationsScreen(false);
          setShowProfileScreen(true);
        }}
      />
    );
  }

  // Show settings screen
  if (showSettingsScreen) {
    return (
      <SettingsScreen
        onBack={() => {
          setShowSettingsScreen(false);
          setShowProfileScreen(true);
        }}
      />
    );
  }

  // Show own profile view
  if (viewingOwnProfile) {
    return (
      <ProfileDetailScreen
        profile={{
          id: currentUser.id,
          name: currentUser.name,
          age: currentUser.age,
          gender: currentUser.gender,
          bio: currentUser.bio,
          height: currentUser.height,
          weight: undefined,
          bodyType: currentUser.bodyType,
          city: currentUser.city,
          country: currentUser.country,
          religion: currentUser.religion,
          sect: undefined,
          maritalStatus: currentUser.maritalStatus,
          education: currentUser.education,
          profession: currentUser.profession,
          incomeRange: currentUser.incomeRange,
          photos: currentUser.photos,
          interests: currentUser.interests,
          hobbies: currentUser.hobbies,
          smoking: currentUser.smoking,
          drinking: currentUser.drinking,
          dietPreference: currentUser.dietPreference,
          familyBackground: currentUser.familyBackground,
          numberOfSiblings: currentUser.numberOfSiblings,
          livingWithFamily: currentUser.livingWithFamily,
          intention: currentUser.intention,
          readyForMarriageTimeframe: currentUser.readyForMarriageTimeframe,
          isVerified: currentUser.isVerified,
        }}
        onBack={() => {
          setViewingOwnProfile(false);
          setShowProfileScreen(true);
        }}
        onMessage={() => {}}
      />
    );
  }

  // Show profile screen
  if (showProfileScreen) {
    return (
      <div className="h-screen bg-white">
        <ProfileScreen
          user={{
            name: currentUser.name,
            age: currentUser.age,
            image: currentUser.image,
            city: currentUser.city,
            profileCompletion: currentUser.profileCompletion,
            matchCount: currentUser.matchCount,
            viewCount: currentUser.viewCount,
          }}
          onViewProfile={() => setViewingOwnProfile(true)}
          onEditProfile={() => setShowEditProfileScreen(true)}
          onSettings={() => setShowSettingsScreen(true)}
          onNotifications={() => setShowNotificationsScreen(true)}
          onPrivacy={() => {
            // Navigate to privacy settings
            console.log('Privacy settings');
          }}
          onSubscription={() => {
            // Navigate to subscription
            console.log('Subscription');
          }}
          onHelp={() => {
            // Navigate to help
            console.log('Help');
          }}
          onAbout={() => {
            // Navigate to about
            console.log('About');
          }}
          onLogout={() => {
            // Handle logout
            console.log('Logout');
          }}
        />
        {/* Bottom Navigation */}
        <div className="absolute bottom-0 left-0 right-0 border-t border-gray-200 px-6 py-4 bg-white">
          <div className="flex items-center justify-around">
            <button className="p-3 text-gray-400 hover:text-pink-500 transition-colors">
              <Heart className="w-6 h-6" />
            </button>
            <button 
              onClick={() => setShowProfileScreen(false)}
              className="p-3 text-gray-400 hover:text-pink-500 transition-colors"
            >
              <MessageCircle className="w-6 h-6" />
            </button>
            <button className="p-3 text-pink-500 transition-colors">
              <User className="w-6 h-6 fill-pink-500" />
            </button>
          </div>
        </div>
      </div>
    );
  }

  // Show profile detail screen
  if (showProfileDetailScreen && selectedMatch) {
    return (
      <ProfileDetailScreen
        profile={{
          id: selectedMatch.id,
          name: selectedMatch.name,
          age: selectedMatch.age,
          gender: selectedMatch.gender || '',
          bio: selectedMatch.bio || '',
          height: selectedMatch.height,
          weight: undefined,
          bodyType: selectedMatch.bodyType,
          city: selectedMatch.city,
          country: selectedMatch.country,
          religion: selectedMatch.religion,
          sect: selectedMatch.sect,
          maritalStatus: selectedMatch.maritalStatus,
          education: selectedMatch.education,
          profession: selectedMatch.profession,
          incomeRange: selectedMatch.incomeRange,
          photos: selectedMatch.photos || [{ url: selectedMatch.image, isPrimary: true }],
          interests: selectedMatch.interests,
          hobbies: selectedMatch.hobbies,
          smoking: selectedMatch.smoking,
          drinking: selectedMatch.drinking,
          dietPreference: selectedMatch.dietPreference,
          familyBackground: selectedMatch.familyBackground,
          numberOfSiblings: selectedMatch.numberOfSiblings,
          livingWithFamily: selectedMatch.livingWithFamily,
          intention: selectedMatch.intention,
          readyForMarriageTimeframe: selectedMatch.readyForMarriageTimeframe,
          isVerified: selectedMatch.isVerified,
        }}
        onBack={() => {
          setShowProfileDetailScreen(false);
          setSelectedMatch(null);
        }}
        onMessage={() => {
          handleMatchMessage(selectedMatch.id);
          setShowProfileDetailScreen(false);
          setSelectedMatch(null);
        }}
        onLike={() => {
          // Handle like action
          console.log('Liked:', selectedMatch.name);
        }}
      />
    );
  }

  // Show match screen
  if (showMatchScreen) {
    return (
      <MatchScreen
        matches={matches}
        onBack={() => setShowMatchScreen(false)}
        onMessage={handleMatchMessage}
        onViewProfile={handleViewProfile}
      />
    );
  }

  if (activeConvo) {
    return (
      <div className="h-screen bg-white">
        <MessageThread
          name={activeConvo.name}
          image={activeConvo.image}
          messages={activeConvo.messages}
          onBack={() => setActiveConversation(null)}
          onSendMessage={handleSendMessage}
        />
      </div>
    );
  }

  return (
    <div className="h-screen bg-white flex flex-col">
      {/* Header */}
      <div className="px-6 py-4 border-b border-gray-200">
        <div className="flex items-center justify-between">
          <Heart className="w-8 h-8 text-pink-500 fill-pink-500" />
          <span className="text-gray-400">Messages</span>
        </div>
      </div>

      {/* New Matches */}
      <div className="px-6 py-4 border-b border-gray-200">
        <div className="flex items-center justify-between mb-3">
          <span className="text-sm text-gray-600">New Matches</span>
          <button
            onClick={() => setShowMatchScreen(true)}
            className="text-xs text-pink-500 hover:text-pink-600 transition-colors"
          >
            See All
          </button>
        </div>
        <div className="flex gap-4 overflow-x-auto pb-2 scrollbar-hide">
          {matches.map((match) => (
            <MatchCard
              key={match.id}
              id={match.id}
              name={match.name}
              image={match.image}
              isNew={match.isNew}
            />
          ))}
        </div>
      </div>

      {/* Conversations */}
      <div className="flex-1 overflow-y-auto">
        <div className="px-6 py-3">
          <span className="text-sm text-gray-600">Messages</span>
        </div>
        {conversations.map((conv) => (
          <ConversationItem
            key={conv.id}
            id={conv.id}
            name={conv.name}
            image={conv.image}
            lastMessage={conv.lastMessage}
            timestamp={conv.timestamp}
            unread={conv.unread}
            onClick={() => setActiveConversation(conv.id)}
          />
        ))}
      </div>

      {/* Bottom Navigation */}
      <div className="border-t border-gray-200 px-6 py-4">
        <div className="flex items-center justify-around">
          <button className="p-3 text-gray-400 hover:text-pink-500 transition-colors">
            <Heart className="w-6 h-6" />
          </button>
          <button className="p-3 text-pink-500 transition-colors">
            <MessageCircle className="w-6 h-6 fill-pink-500" />
          </button>
          <button 
            onClick={() => setShowProfileScreen(true)}
            className="p-3 text-gray-400 hover:text-pink-500 transition-colors"
          >
            <User className="w-6 h-6" />
          </button>
        </div>
      </div>
    </div>
  );
}